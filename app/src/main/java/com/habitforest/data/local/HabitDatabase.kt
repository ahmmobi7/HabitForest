package com.habitforest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.habitforest.data.model.Habit
import com.habitforest.data.model.HabitLog
import com.habitforest.data.model.Reward
import com.habitforest.data.model.User

@Database(
    entities = [Habit::class, HabitLog::class, User::class, Reward::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
