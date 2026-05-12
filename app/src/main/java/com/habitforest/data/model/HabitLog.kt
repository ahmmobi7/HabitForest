package com.habitforest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "logs",
    primaryKeys = ["habit_id", "date"]
)
data class HabitLog(
    @ColumnInfo(name = "habit_id")
    @SerialName("habit_id")
    val habitId: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = "",

    @ColumnInfo(name = "date")
    @SerialName("date")
    val date: String = "",

    @ColumnInfo(name = "status")
    @SerialName("status")
    val status: String = ""   // "YES" | "NO"
)
