package com.habitforest.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.habitforest.data.model.Habit
import com.habitforest.ui.theme.*

@Composable
fun ForestPreviewCard(habits: List<Habit>, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape           = RoundedCornerShape(20.dp),
        shadowElevation = 6.dp
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF0D2B1D), DarkMoss, EarthBrown.copy(0.6f))))
        ) {
            // Ground strip
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .align(Alignment.BottomCenter)
                    .background(EarthBrown.copy(alpha = 0.45f))
            )

            if (habits.isEmpty()) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    Text("🌱", fontSize = 36.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Your forest is empty", color = SkyBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Plant habits to see trees grow!", color = MossGreen, fontSize = 11.sp)
                }
            } else {
                // Trees laid out bottom-up in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 36.dp),
                    verticalArrangement   = Arrangement.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    userScrollEnabled = false
                ) {
                    items(habits.take(12)) { habit ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(habit.treeEmoji, fontSize = 26.sp)
                        }
                    }
                }
            }

            // Top-right label
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                color = NightForest.copy(alpha = 0.7f)
            ) {
                Text(
                    "View Forest →",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = SkyBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tree count
            if (habits.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = NightForest.copy(alpha = 0.7f)
                ) {
                    Text(
                        "🌲 ${habits.size} trees",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = MossGreen,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
