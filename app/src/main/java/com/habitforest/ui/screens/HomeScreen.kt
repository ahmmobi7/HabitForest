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
import androidx.compose.ui.graphics.Brush
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
                containerColor = LeafGreen,
                contentColor = NightForest
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
        containerColor = ParchmentBg
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Today's Habits",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                    val done = state.todayLogs.values.count { it == "YES" }
                    Text(
                        "$done/${state.habits.size} done",
                        fontSize = 13.sp,
                        color = EarthBrown
                    )
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
                        "Insights",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
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
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = ForestGreen,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = state.toast ?: "",
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp),
                        color = SkyBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(user: User?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = ForestGreen,
        shadowElevation = 4.dp
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("🌳 Habit Forest", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = SkyBlue)
                    Text("Keep growing every day!", fontSize = 13.sp, color = MossGreen)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("⚡ ${user?.xp ?: 0} XP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldenYes)
                    Text("Level ${user?.level ?: 1}", fontSize = 13.sp, color = MossGreen)
                }
            }
            Spacer(Modifier.height(12.dp))
            // XP progress bar
            val progress = user?.progressToNextLevel ?: 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = GoldenYes,
                trackColor = DarkMoss
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${(progress * 100).toInt()}% to Level ${(user?.level ?: 1) + 1}",
                fontSize = 11.sp,
                color = MossGreen
            )
        }
    }
}

@Composable
private fun EmptyHabitsCard(onAdd: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LightSurface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🌱", fontSize = 52.sp)
            Spacer(Modifier.height(12.dp))
            Text("Your forest awaits!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreen)
            Spacer(Modifier.height(4.dp))
            Text("Tap + to plant your first habit", fontSize = 14.sp, color = EarthBrown)
            Spacer(Modifier.height(20.dp))
            OutlinedButton(
                onClick = onAdd,
                border = BorderStroke(1.5.dp, LeafGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add First Habit 🌱", color = ForestGreen)
            }
        }
    }
}

@Composable
private fun ForestBottomBar(onForest: () -> Unit, onCheckIn: () -> Unit, onRewards: () -> Unit) {
    NavigationBar(containerColor = NightForest, tonalElevation = 0.dp) {
        NavigationBarItem(
            selected = false,
            onClick  = onCheckIn,
            icon     = { Text("✅", fontSize = 22.sp) },
            label    = { Text("Check In", color = MossGreen, fontSize = 11.sp) },
            colors   = NavigationBarItemDefaults.colors(indicatorColor = DarkMoss)
        )
        NavigationBarItem(
            selected = true,
            onClick  = onForest,
            icon     = { Text("🌳", fontSize = 22.sp) },
            label    = { Text("Forest", color = LeafGreen, fontSize = 11.sp) },
            colors   = NavigationBarItemDefaults.colors(indicatorColor = DarkMoss)
        )
        NavigationBarItem(
            selected = false,
            onClick  = onRewards,
            icon     = { Text("🏆", fontSize = 22.sp) },
            label    = { Text("Rewards", color = MossGreen, fontSize = 11.sp) },
            colors   = NavigationBarItemDefaults.colors(indicatorColor = DarkMoss)
        )
    }
}
