package com.habitforest.data.local

import androidx.room.*
import com.habitforest.data.model.Habit
import com.habitforest.data.model.HabitLog
import com.habitforest.data.model.Reward
import com.habitforest.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // ─── User ───────────────────────────────────────────────────────────────
    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun observeUser(id: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUser(id: String): User?

    // ─── Habits ─────────────────────────────────────────────────────────────
    @Upsert
    suspend fun upsertHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE user_id = :userId ORDER BY created_at ASC")
    fun observeHabits(userId: String): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabit(id: String): Habit?

    @Delete
    suspend fun deleteHabit(habit: Habit)

    // ─── Logs ───────────────────────────────────────────────────────────────
    @Upsert
    suspend fun upsertLog(log: HabitLog)

    @Query("SELECT * FROM logs WHERE habit_id = :habitId ORDER BY date DESC")
    fun observeLogs(habitId: String): Flow<List<HabitLog>>

    @Query("SELECT * FROM logs WHERE habit_id = :habitId AND date = :date LIMIT 1")
    suspend fun getLogForDate(habitId: String, date: String): HabitLog?

    @Query("SELECT * FROM logs WHERE user_id = :userId AND date >= :fromDate ORDER BY date DESC")
    suspend fun getLogsFrom(userId: String, fromDate: String): List<HabitLog>

    // ─── Rewards ────────────────────────────────────────────────────────────
    @Upsert
    suspend fun upsertReward(reward: Reward)

    @Query("SELECT * FROM rewards WHERE user_id = :userId")
    fun observeRewards(userId: String): Flow<List<Reward>>

    @Query("SELECT * FROM rewards WHERE user_id = :userId AND name = :name LIMIT 1")
    suspend fun getRewardByName(userId: String, name: String): Reward?
}
