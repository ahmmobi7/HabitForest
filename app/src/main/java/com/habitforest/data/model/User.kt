package com.habitforest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerialName("id")
    val id: String = "",

    @ColumnInfo(name = "xp")
    @SerialName("xp")
    val xp: Int = 0,

    @ColumnInfo(name = "level")
    @SerialName("level")
    val level: Int = 1
) {
    companion object {
        fun xpForNextLevel(level: Int): Int = level * 100
    }

    val progressToNextLevel: Float
        get() {
            val needed = xpForNextLevel(level)
            val current = xp % needed
            return current.toFloat() / needed.toFloat()
        }
}
