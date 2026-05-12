package com.habitforest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerialName("id")
    val id: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = "",

    @ColumnInfo(name = "type")
    @SerialName("type")
    val type: String = "",        // "animal" | "land"

    @ColumnInfo(name = "name")
    @SerialName("name")
    val name: String = "",

    @ColumnInfo(name = "unlocked_at")
    @SerialName("unlocked_at")
    val unlockedAt: String = ""
)

data class RewardDefinition(val name: String, val emoji: String, val threshold: Int, val type: String)

object RewardCatalogue {
    val ALL = listOf(
        RewardDefinition("Deer",     "🦌", 1,    "animal"),
        RewardDefinition("Squirrel", "🐿️", 2,   "animal"),
        RewardDefinition("Fox",      "🦊", 3,    "animal"),
        RewardDefinition("Bear",     "🐻", 5,    "animal"),
        RewardDefinition("Eagle",    "🦅", 8,    "animal"),
        RewardDefinition("Dragon",   "🐉", 15,   "animal"),
        RewardDefinition("Meadow",   "🏕️", 50,   "land"),
        RewardDefinition("Mountain", "🏔️", 200,  "land"),
        RewardDefinition("Lakeside", "🌊", 500,  "land"),
        RewardDefinition("Volcano",  "🌋", 1000, "land")
    )
}
