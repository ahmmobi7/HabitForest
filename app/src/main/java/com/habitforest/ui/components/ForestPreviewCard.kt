package com.habitforest.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.habitforest.data.model.Habit
import com.habitforest.ui.theme.*

@Composable
fun ForestPreviewCard(habits: List<Habit>, onClick: () -> Unit) {
    GamePanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF0D2B1D), DarkMoss, EarthBrown.copy(0.6f))))
        ) {
            // Ground strip
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(EarthBrown.copy(alpha = 0.45f))
            )

            if (habits.isEmpty()) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    Text("🏜️", fontSize = 42.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("DESERTED REALM", style = MaterialTheme.typography.titleLarge, color = GameGold)
                    Text("Plant habits to grow your kingdom", style = MaterialTheme.typography.labelMedium, color = SkyBlue)
                }
            } else {
                // Trees laid out bottom-up in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 40.dp),
                    verticalArrangement   = Arrangement.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    userScrollEnabled = false
                ) {
                    items(habits.take(12)) { habit ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(habit.treeEmoji, fontSize = 28.sp)
                        }
                    }
                }
            }

            // Top-right label
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                color = NightForest.copy(alpha = 0.8f)
            ) {
                Text(
                    "EXPLORE →",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = GameGold,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Tree count
            if (habits.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = NightForest.copy(alpha = 0.8f)
                ) {
                    Text(
                        "🏰 ${habits.size} TERRITORIES",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MossGreen,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
