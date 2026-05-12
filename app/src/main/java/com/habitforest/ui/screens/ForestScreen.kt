package com.habitforest.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitforest.data.model.GrowthStage
import com.habitforest.data.model.Habit
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForestScreen(
    onBack: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var scale   by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🌳 My Forest", color = SkyBlue, fontWeight = FontWeight.Bold)
                        Text("Pinch to zoom · tap trees for info", color = MossGreen, fontSize = 11.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = SkyBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkMoss)
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        0f to Color(0xFF87CEEB),
                        0.5f to DarkMoss,
                        1f to EarthBrown
                    )
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale   = (scale * zoom).coerceIn(0.4f, 4f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            // Clouds
            AnimatedClouds()

            // Zoomable forest
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX       = scale,
                        scaleY       = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
            ) {
                ForestGrid(habits = state.habits)
            }

            // Stage legend overlay
            StageLegend(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )

            // Habit count chip
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                shape    = RoundedCornerShape(20.dp),
                color    = NightForest.copy(alpha = 0.8f)
            ) {
                Text(
                    "🌲 ${state.habits.size} trees",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = SkyBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AnimatedClouds() {
    val inf = rememberInfiniteTransition(label = "clouds")
    val dx  by inf.animateFloat(0f, 20f, infiniteRepeatable(tween(6000), RepeatMode.Reverse), label = "")
    Text("☁️", Modifier.offset((60 + dx).dp, 12.dp), fontSize = 36.sp)
    Text("☁️", Modifier.offset((180 - dx / 2).dp, 28.dp), fontSize = 22.sp)
    Text("☁️", Modifier.offset((290 + dx / 3).dp, 8.dp), fontSize = 44.sp)
}

@Composable
private fun ForestGrid(habits: List<Habit>) {
    if (habits.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌱", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("No trees yet — go plant some habits!", color = SkyBlue, fontSize = 14.sp)
            }
        }
        return
    }

    val cols = 5
    Column(
        modifier = Modifier
            .padding(top = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        habits.chunked(cols).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                row.forEach { habit -> TreeTile(habit) }
                // Fill empty cols in last row
                repeat(cols - row.size) { Box(Modifier.size(64.dp)) }
            }
        }
    }
}

@Composable
private fun TreeTile(habit: Habit) {
    var showInfo by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (showInfo) 1.15f else 1f,
        spring(Spring.DampingRatioMediumBouncy),
        label = ""
    )

    Column(
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable { showInfo = !showInfo },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (showInfo) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = NightForest.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    "${habit.name}\n🔥 ${habit.streak}d",
                    Modifier.padding(4.dp),
                    color = SkyBlue,
                    fontSize = 8.sp,
                    lineHeight = 11.sp
                )
            }
        }
        Text(habit.treeEmoji, fontSize = 36.sp)
    }
}

@Composable
private fun StageLegend(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = NightForest.copy(alpha = 0.85f)
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text("Stages", color = SkyBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            GrowthStage.entries.forEach { s ->
                Text("${s.emoji} ${s.label}", color = MossGreen, fontSize = 10.sp)
            }
        }
    }
}
