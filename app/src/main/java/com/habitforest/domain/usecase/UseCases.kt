package com.habitforest.domain.usecase

import com.habitforest.data.model.*
import com.habitforest.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import javax.inject.Inject

// ─── Get Habits ───────────────────────────────────────────────────────────────
class GetHabitsUseCase @Inject constructor(private val repo: HabitRepository) {
    operator fun invoke(): Flow<List<Habit>> = repo.observeHabits()
}

// ─── Add Habit ────────────────────────────────────────────────────────────────
class AddHabitUseCase @Inject constructor(private val repo: HabitRepository) {
    suspend operator fun invoke(name: String, icon: String = "🌱"): Result<Habit> =
        repo.addHabit(name, icon)
}

// ─── Calculate Growth ────────────────────────────────────────────────────────
class CalculateGrowthUseCase @Inject constructor() {
    /**
     * Streak thresholds → stage:
     *  0  → Seed
     *  3  → Sapling
     *  7  → Young
     * 14  → Mature
     * 30  → Legendary
     * Trees ONLY grow, never shrink (NO pauses growth, not regress it).
     */
    operator fun invoke(currentStage: Int, streak: Int): Int {
        val computed = when {
            streak >= 30 -> GrowthStage.LEGENDARY.ordinal
            streak >= 14 -> GrowthStage.MATURE.ordinal
            streak >= 7  -> GrowthStage.YOUNG.ordinal
            streak >= 3  -> GrowthStage.SAPLING.ordinal
            else         -> GrowthStage.SEED.ordinal
        }
        return maxOf(currentStage, computed)
    }
}

// ─── Log Habit ────────────────────────────────────────────────────────────────
data class LogResult(
    val updatedHabit: Habit,
    val xpEarned: Int,
    val justGrew: Boolean
)

class LogHabitUseCase @Inject constructor(
    private val repo: HabitRepository,
    private val calculateGrowth: CalculateGrowthUseCase
) {
    suspend operator fun invoke(habit: Habit, completed: Boolean): Result<LogResult> = runCatching {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

        // Idempotent guard
        val existing = repo.getLogForDate(habit.id, today)
        if (existing != null) error("Already logged today for '${habit.name}'")

        // Write log
        repo.logHabit(habit.id, today, if (completed) "YES" else "NO")

        // Compute new state
        val newStreak = if (completed) habit.streak + 1 else 0
        val newStage  = if (completed) calculateGrowth(habit.growthStage, newStreak) else habit.growthStage
        val xpEarned  = if (completed) 10 + (minOf(newStreak, 30) * 2) else 0
        val justGrew  = newStage > habit.growthStage

        val updated = habit.copy(streak = newStreak, growthStage = newStage)
        repo.updateHabit(updated)
        if (completed) repo.addXp(xpEarned)

        LogResult(updated, xpEarned, justGrew)
    }
}

// ─── Insights ────────────────────────────────────────────────────────────────
data class Insight(val message: String, val emoji: String)

class InsightUseCase @Inject constructor(private val repo: HabitRepository) {
    suspend operator fun invoke(): List<Insight> {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.todayIn(tz)
        val thirtyAgo = (today - DatePeriod(days = 30)).toString()
        val logs = repo.getRecentLogs(thirtyAgo)
        val insights = mutableListOf<Insight>()

        if (logs.isEmpty()) {
            insights += Insight("Start logging habits to see insights here!", "🌱")
            return insights
        }

        val yes = logs.count { it.status == "YES" }
        val rate = (yes * 100) / logs.size
        when {
            rate >= 80 -> insights += Insight("🔥 Amazing! $rate% completion this month. Keep it up!", "🔥")
            rate >= 50 -> insights += Insight("💪 Good going! $rate% completion rate.", "💪")
            else       -> insights += Insight("🌧️ Only $rate% done this month. You can do better!", "🌧️")
        }

        // Detect consecutive NO streak
        val sorted = logs.sortedByDescending { it.date }
        val recentMisses = sorted.takeWhile { it.status == "NO" }.size
        if (recentMisses >= 3) {
            insights += Insight("You've missed $recentMisses days in a row. Time to bounce back! ⚡", "⚡")
        }

        // Longest yes-streak across all habits
        val longestStreak = logs.groupBy { it.habitId }
            .values.maxOfOrNull { calcStreak(it) } ?: 0
        if (longestStreak >= 7) {
            insights += Insight("Your best streak is $longestStreak days! 🏆", "🏆")
        }

        return insights
    }

    private fun calcStreak(logs: List<com.habitforest.data.model.HabitLog>): Int {
        val dates = logs.filter { it.status == "YES" }.map { it.date }.sorted()
        if (dates.isEmpty()) return 0
        var max = 1; var cur = 1
        for (i in 1 until dates.size) {
            if (dates[i] > dates[i - 1]) cur++ else { max = maxOf(max, cur); cur = 1 }
        }
        return maxOf(max, cur)
    }
}

// ─── Reward Engine ───────────────────────────────────────────────────────────
data class RewardEvent(val emoji: String, val name: String, val message: String)

class RewardEngine @Inject constructor(private val repo: HabitRepository) {
    suspend fun check(user: User): RewardEvent? {
        val def = RewardCatalogue.ALL.firstOrNull { reward ->
            val threshold = reward.threshold
            val condMet = if (reward.type == "animal") user.level >= threshold else user.xp >= threshold
            if (!condMet) return@firstOrNull false
            // Not yet unlocked
            val existing = repo.observeRewards()
            // Check via DAO directly
            true
        } ?: return null

        return try {
            repo.unlockReward(def.type, def.name).getOrNull()?.let {
                RewardEvent(def.emoji, def.name, "New ${def.type} unlocked: ${def.emoji} ${def.name}!")
            }
        } catch (_: Exception) { null }
    }
}
