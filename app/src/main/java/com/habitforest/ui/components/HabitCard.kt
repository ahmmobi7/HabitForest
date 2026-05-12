package com.habitforest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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

    val cardColor = when (todayStatus) {
        "YES" -> LeafGreen.copy(alpha = 0.14f)
        "NO"  -> CoralNo.copy(alpha = 0.08f)
        else  -> LightSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale),
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        shadowElevation = if (logged) 2.dp else 4.dp,
        border = if (todayStatus == "YES")
            androidx.compose.foundation.BorderStroke(1.dp, LeafGreen.copy(0.3f))
        else null
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tree visual
            Text(habit.treeEmoji, fontSize = 36.sp)

            Spacer(Modifier.width(12.dp))

            // Info
            Column(Modifier.weight(1f)) {
                Text(
                    habit.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = NightForest,
                    maxLines   = 1
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${habit.icon}  ", fontSize = 13.sp)
                    Text("🔥 ${habit.streak}d", fontSize = 12.sp, color = EarthBrown)
                    Text("  ·  ", fontSize = 12.sp, color = MossGreen)
                    Text(habit.stage.label, fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.width(8.dp))

            // Action area
            if (logged) {
                Text(
                    if (todayStatus == "YES") "✅ Done!" else "⏭ Skipped",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = if (todayStatus == "YES") ForestGreen else EarthBrown
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // NO button
                    OutlinedButton(
                        onClick      = { bouncing = true; onNo() },
                        modifier     = Modifier.size(48.dp, 36.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape        = RoundedCornerShape(10.dp),
                        border       = androidx.compose.foundation.BorderStroke(1.dp, CoralNo)
                    ) {
                        Text("✗", fontSize = 16.sp, color = CoralNo)
                    }
                    // YES button
                    Button(
                        onClick      = { bouncing = true; onYes() },
                        modifier     = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        shape        = RoundedCornerShape(10.dp),
                        colors       = ButtonDefaults.buttonColors(containerColor = LeafGreen)
                    ) {
                        Text("YES ✓", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NightForest)
                    }
                }
            }
        }
    }
}
