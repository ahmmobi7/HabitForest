package com.habitforest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitforest.ui.components.HabitCard
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.HomeViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    onBack: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val done  = state.todayLogs.values.count { it == "YES" }
    val total = state.habits.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Check-In ✅", color = SkyBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = SkyBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkMoss)
            )
        },
        containerColor = ParchmentBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(16.dp),
                    color    = ForestGreen,
                    shadowElevation = 4.dp
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "${today.dayOfMonth} ${today.month.name.take(3)} ${today.year}",
                            color = SkyBlue, fontSize = 13.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "$done / $total habits done",
                            color = LightSurface, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { if (total > 0) done.toFloat() / total else 0f },
                            modifier = Modifier.fillMaxWidth().height(10.dp),
                            color = GoldenYes,
                            trackColor = DarkMoss
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            when {
                                total == 0  -> "Add some habits to get started! 🌱"
                                done == total -> "All done! 🎉 Great job today!"
                                done == 0   -> "Tap YES to log your first habit today!"
                                else        -> "${total - done} left — you got this! 💪"
                            },
                            color = MossGreen, fontSize = 13.sp
                        )
                    }
                }
            }

            if (state.habits.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No habits yet.\nGo to Home → + to add one 🌱",
                            color = EarthBrown, fontSize = 15.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }

            items(state.habits, key = { it.id }) { habit ->
                HabitCard(
                    habit       = habit,
                    todayStatus = state.todayLogs[habit.id] ?: "",
                    onYes       = { vm.logHabitToday(habit, true) },
                    onNo        = { vm.logHabitToday(habit, false) }
                )
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}
