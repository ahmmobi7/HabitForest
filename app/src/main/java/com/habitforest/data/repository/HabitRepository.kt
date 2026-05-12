package com.habitforest.data.repository

import com.habitforest.data.local.HabitDao
import com.habitforest.data.model.*
import com.habitforest.data.remote.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val dao: HabitDao
) {
    // ─── Auth helpers ────────────────────────────────────────────────────────
    fun currentUserId(): String = supabase.auth.currentUserOrNull()?.id ?: ""
    fun isLoggedIn(): Boolean = supabase.auth.currentUserOrNull() != null

    // ─── Anonymous sign-in ───────────────────────────────────────────────────
    suspend fun signInAnonymously(): Result<Unit> = runCatching {
        supabase.auth.signInAnonymously()
        val uid = supabase.auth.currentUserOrNull()?.id ?: return@runCatching
        // Ensure local user row exists
        val existing = dao.getUser(uid)
        if (existing == null) {
            val user = User(id = uid)
            dao.upsertUser(user)
            // Also push to Supabase (trigger handles it, but belt-and-suspenders)
            try { supabase.from("users").upsert(user) } catch (_: Exception) {}
        }
    }

    suspend fun signOut(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }

    // ─── User ────────────────────────────────────────────────────────────────
    fun observeUser(): Flow<User?> = dao.observeUser(currentUserId())

    suspend fun addXp(xpDelta: Int) {
        val uid = currentUserId().ifEmpty { return }
        val user = dao.getUser(uid) ?: User(id = uid)
        val newXp = user.xp + xpDelta
        val needed = User.xpForNextLevel(user.level)
        val newLevel = if (newXp >= needed) user.level + 1 else user.level
        val updated = user.copy(xp = newXp, level = newLevel)
        dao.upsertUser(updated)
        try {
            supabase.from("users").upsert(updated)
        } catch (_: Exception) {
            // Offline — Room is source of truth, will sync later
        }
    }

    // ─── Habits ──────────────────────────────────────────────────────────────
    fun observeHabits(): Flow<List<Habit>> = dao.observeHabits(currentUserId())

    suspend fun addHabit(name: String, icon: String): Result<Habit> = runCatching {
        require(name.isNotBlank()) { "Name cannot be empty" }
        require(name.length <= 50) { "Name too long (max 50 chars)" }
        val now = Clock.System.now().toString()
        val habit = Habit(
            id = UUID.randomUUID().toString(),
            userId = currentUserId(),
            name = name.trim(),
            icon = icon,
            createdAt = now
        )
        dao.upsertHabit(habit)
        try { supabase.from("habits").insert(habit) } catch (_: Exception) {}
        habit
    }

    suspend fun updateHabit(habit: Habit): Result<Unit> = runCatching {
        dao.upsertHabit(habit)
        try { supabase.from("habits").upsert(habit) } catch (_: Exception) {}
    }

    suspend fun deleteHabit(habit: Habit): Result<Unit> = runCatching {
        dao.deleteHabit(habit)
        try {
            supabase.from("habits").delete { filter { eq("id", habit.id) } }
        } catch (_: Exception) {}
    }

    // ─── Logs ────────────────────────────────────────────────────────────────
    suspend fun logHabit(habitId: String, date: String, status: String): Result<Unit> = runCatching {
        val log = HabitLog(
            habitId = habitId,
            userId = currentUserId(),
            date = date,
            status = status
        )
        dao.upsertLog(log)
        try { supabase.from("logs").upsert(log) } catch (_: Exception) {}
    }

    suspend fun getLogForDate(habitId: String, date: String): HabitLog? =
        dao.getLogForDate(habitId, date)

    suspend fun getRecentLogs(fromDate: String): List<HabitLog> =
        dao.getLogsFrom(currentUserId(), fromDate)

    // ─── Rewards ─────────────────────────────────────────────────────────────
    fun observeRewards(): Flow<List<Reward>> = dao.observeRewards(currentUserId())

    suspend fun unlockReward(type: String, name: String): Result<Reward> = runCatching {
        // Idempotent — don't unlock twice
        val existing = dao.getRewardByName(currentUserId(), name)
        if (existing != null) return@runCatching existing
        val reward = Reward(
            id = UUID.randomUUID().toString(),
            userId = currentUserId(),
            type = type,
            name = name,
            unlockedAt = Clock.System.now().toString()
        )
        dao.upsertReward(reward)
        try { supabase.from("rewards").insert(reward) } catch (_: Exception) {}
        reward
    }
}
