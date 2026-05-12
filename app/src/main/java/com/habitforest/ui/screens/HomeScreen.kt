package com.habitforest.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitforest.data.model.Habit
import com.habitforest.data.model.User
import com.habitforest.ui.components.*
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToForest: () -> Unit,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToRewards: () -> Unit,
    onAddHabit: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    // Auto-dismiss toast
    LaunchedEffect(state.toast) {
        if (state.toast != null) { delay(2500); vm.dismissToast() }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabit,
                containerColor = GameOrange,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        },
        bottomBar = {
            ForestBottomBar(
                onForest  = onNavigateToForest,
                onCheckIn = onNavigateToCheckIn,
                onRewards = onNavigateToRewards
            )
        },
        containerColor = GameParchment
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Header XP bar ──────────────────────────────────────────────
            item { HeaderCard(state.user) }

            // ── Mini forest preview ───────────────────────────────────────
            item {
                ForestPreviewCard(habits = state.habits, onClick = onNavigateToForest)
            }

            // ── Today title ───────────────────────────────────────────────
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Active Quests",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepWood
                    )
                    val done = state.todayLogs.values.count { it == "YES" }
                    Surface(
                        color = SoftWood.copy(0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "$done/${state.habits.size} completed",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = DeepWood
                        )
                    }
                }
            }

            // ── Empty state ───────────────────────────────────────────────
            if (state.habits.isEmpty()) {
                item { EmptyHabitsCard(onAddHabit) }
            }

            // ── Habit cards ───────────────────────────────────────────────
            items(state.habits, key = { it.id }) { habit ->
                HabitCard(
                    habit       = habit,
                    todayStatus = state.todayLogs[habit.id] ?: "",
                    onYes       = { vm.logHabitToday(habit, true) },
                    onNo        = { vm.logHabitToday(habit, false) }
                )
            }

            // ── Insights ──────────────────────────────────────────────────
            if (state.insights.isNotEmpty()) {
                item {
                    Text(
                        "Battle Log",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepWood
                    )
                }
                items(state.insights) { insight ->
                    InsightCard(insight)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        // ── Reward overlay ────────────────────────────────────────────────
        state.rewardEvent?.let { event ->
            RewardOverlay(event = event, onDismiss = { vm.dismissReward() })
        }

        // ── Toast ─────────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = state.toast != null,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp),
            enter = fadeIn() + slideInVertically { it },
            exit  = fadeOut() + slideOutVertically { it }
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                GamePanel(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = state.toast ?: "",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepWood
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(user: User?) {
    GamePanel(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "HERO STATS",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoftWood
                )
                Text(
                    "Level ${user?.level ?: 1}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepWood
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "TOTAL XP",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoftWood
                )
                Text(
                    "${user?.xp ?: 0}",
                    style = MaterialTheme.typography.titleLarge,
                    color = GameOrange
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // XP Progress Bar
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("XP Progress", style = MaterialTheme.typography.labelMedium)
                Text("${((user?.progressToNextLevel ?: 0f) * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DeepWood.copy(0.1f))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(user?.progressToNextLevel ?: 0f)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(listOf(GameGold, GameOrange))
                        )
                )
            }
        }
    }
}

@Composable
private fun EmptyHabitsCard(onAdd: () -> Unit) {
    GamePanel(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("📜", fontSize = 52.sp)
            Spacer(Modifier.height(12.dp))
            Text("Quest Board Empty", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text("Visit the guild master to plant a habit", style = MaterialTheme.typography.bodyLarge, color = SoftWood)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(containerColor = DeepWood),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Start New Quest", color = GameGold)
            }
        }
    }
}

@Composable
private fun ForestBottomBar(onForest: () -> Unit, onCheckIn: () -> Unit, onRewards: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DeepWood,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(72.dp)
        ) {
            NavigationBarItem(
                selected = false,
                onClick  = onCheckIn,
                icon     = { Text("⚔️", fontSize = 24.sp) },
                label    = { Text("Quests", style = MaterialTheme.typography.labelMedium, color = GameParchment) },
                colors   = NavigationBarItemDefaults.colors(indicatorColor = SoftWood)
            )
            NavigationBarItem(
                selected = true,
                onClick  = onForest,
                icon     = { Text("🌲", fontSize = 24.sp) },
                label    = { Text("Kingdom", style = MaterialTheme.typography.labelMedium, color = GameParchment) },
                colors   = NavigationBarItemDefaults.colors(indicatorColor = SoftWood)
            )
            NavigationBarItem(
                selected = false,
                onClick  = onRewards,
                icon     = { Text("💰", fontSize = 24.sp) },
                label    = { Text("Treasury", style = MaterialTheme.typography.labelMedium, color = GameParchment) },
                colors   = NavigationBarItemDefaults.colors(indicatorColor = SoftWood)
            )
        }
    }
}
