package com.habitforest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.habitforest.data.model.Habit
import com.habitforest.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HabitCard(
    habit: Habit,
    todayStatus: String,
    onYes: () -> Unit,
    onNo: () -> Unit
) {
    val logged = todayStatus.isNotEmpty()
    var bouncing by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (bouncing) 1.06f else 1f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bounce"
    )

    LaunchedEffect(bouncing) {
        if (bouncing) { delay(200); bouncing = false }
    }

    val borderColor = when (todayStatus) {
        "YES" -> LeafGreen
        "NO"  -> HealthRed
        else  -> BorderBrown
    }

    GameCard(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale),
        borderColor = borderColor
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tree visual or Quest Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer(rotationZ = if (todayStatus == "YES") 10f else 0f),
                contentAlignment = Alignment.Center
            ) {
                Text(habit.treeEmoji, fontSize = 32.sp)
            }

            Spacer(Modifier.width(12.dp))

            // Info
            Column(Modifier.weight(1f)) {
                Text(
                    habit.name.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepWood,
                    maxLines   = 1
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔥 ${habit.streak} DAY STREAK", style = MaterialTheme.typography.labelMedium, color = GameOrange)
                    Text("  •  ", style = MaterialTheme.typography.labelMedium, color = SoftWood)
                    Text(habit.stage.label.uppercase(), style = MaterialTheme.typography.labelMedium, color = ForestGreen)
                }
            }

            Spacer(Modifier.width(8.dp))

            // Action area
            if (logged) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        if (todayStatus == "YES") "COMPLETED" else "FAILED",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (todayStatus == "YES") ForestGreen else HealthRed
                    )
                    Text(
                        if (todayStatus == "YES") "+10 XP" else "+0 XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (todayStatus == "YES") GameGold else SoftWood
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // NO button
                    IconButton(
                        onClick = { bouncing = true; onNo() },
                        modifier = Modifier
                            .size(36.dp)
                            .background(HealthRed.copy(0.1f), RoundedCornerShape(8.dp))
                    ) {
                        Text("✕", color = HealthRed, fontWeight = FontWeight.Bold)
                    }

                    // YES button
                    Button(
                        onClick      = { bouncing = true; onYes() },
                        modifier     = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        shape        = RoundedCornerShape(8.dp),
                        colors       = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                    ) {
                        Text("CLAIM ✓", style = MaterialTheme.typography.labelMedium, color = Color.White)
                    }
                }
            }
        }
    }
}
