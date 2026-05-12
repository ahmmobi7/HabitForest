package com.habitforest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class GrowthStage(val label: String, val emoji: String) {
    SEED("Seed", "🌱"),
    SAPLING("Sapling", "🌿"),
    YOUNG("Young Tree", "🌲"),
    MATURE("Mature", "🌳"),
    LEGENDARY("Legendary", "🌴");

    companion object {
        fun fromOrdinal(n: Int): GrowthStage = entries.getOrElse(n) { SEED }
    }
}

@Serializable
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerialName("id")
    val id: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = "",

    @ColumnInfo(name = "name")
    @SerialName("name")
    val name: String = "",

    @ColumnInfo(name = "icon")
    @SerialName("icon")
    val icon: String = "🌱",

    @ColumnInfo(name = "frequency")
    @SerialName("frequency")
    val frequency: String = "daily",

    @ColumnInfo(name = "streak")
    @SerialName("streak")
    val streak: Int = 0,

    @ColumnInfo(name = "growth_stage")
    @SerialName("growth_stage")
    val growthStage: Int = 0,

    @ColumnInfo(name = "created_at")
    @SerialName("created_at")
    val createdAt: String = ""
) {
    val stage: GrowthStage get() = GrowthStage.fromOrdinal(growthStage)

    val treeEmoji: String get() = when (growthStage) {
        0 -> "🌱"
        1 -> "🌿"
        2 -> "🌲"
        3 -> "🌳"
        4 -> "🌴"
        else -> "🌱"
    }
}
