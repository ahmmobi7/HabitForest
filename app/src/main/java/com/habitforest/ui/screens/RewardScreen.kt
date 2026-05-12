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
import com.habitforest.data.model.RewardCatalogue
import com.habitforest.data.model.User
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.RewardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    onBack: () -> Unit,
    vm: RewardViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val unlockedNames = state.unlockedRewards.map { it.name }.toSet()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Rewards", color = SkyBlue, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // XP / level progress card
            item { XpProgressCard(state.user) }

            // Unlocked rewards
            if (state.unlockedRewards.isNotEmpty()) {
                item { SectionLabel("🎉 Unlocked") }
                items(state.unlockedRewards) { r ->
                    RewardRow(emoji = r.name.split(" ").firstOrNull() ?: "🎁",
                        name = r.name, subtitle = r.type.replaceFirstChar { it.uppercase() },
                        unlocked = true)
                }
            }

            // Animals catalogue
            item { SectionLabel("🦌 Animals") }
            items(RewardCatalogue.ALL.filter { it.type == "animal" }) { def ->
                val ok = (state.user?.level ?: 0) >= def.threshold
                val alreadyIn = unlockedNames.contains(def.name)
                RewardRow(
                    emoji    = def.emoji,
                    name     = "${def.emoji} ${def.name}",
                    subtitle = if (ok || alreadyIn) "Unlocked at Level ${def.threshold}" else "Reach Level ${def.threshold}",
                    unlocked = ok || alreadyIn
                )
            }

            // Land catalogue
            item { SectionLabel("🏕️ Lands") }
            items(RewardCatalogue.ALL.filter { it.type == "land" }) { def ->
                val ok = (state.user?.xp ?: 0) >= def.threshold
                val alreadyIn = unlockedNames.contains(def.name)
                RewardRow(
                    emoji    = def.emoji,
                    name     = "${def.emoji} ${def.name}",
                    subtitle = if (ok || alreadyIn) "Unlocked at ${def.threshold} XP" else "Need ${def.threshold} XP",
                    unlocked = ok || alreadyIn
                )
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun XpProgressCard(user: User?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(20.dp),
        color    = DarkMoss,
        shadowElevation = 4.dp
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Level ${user?.level ?: 1}", color = GoldenYes, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                Text("⚡ ${user?.xp ?: 0} XP total", color = SkyBlue, fontSize = 14.sp)
            }
            LinearProgressIndicator(
                progress = { user?.progressToNextLevel ?: 0f },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color    = GoldenYes,
                trackColor = ForestGreen.copy(alpha = 0.3f)
            )
            val pct = ((user?.progressToNextLevel ?: 0f) * 100).toInt()
            Text(
                "$pct% to Level ${(user?.level ?: 1) + 1}  ·  Next: ${User.xpForNextLevel(user?.level ?: 1)} XP",
                color = MossGreen, fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreen,
        modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun RewardRow(emoji: String, name: String, subtitle: String, unlocked: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        color    = if (unlocked) LeafGreen.copy(0.12f) else androidx.compose.ui.graphics.Color.LightGray.copy(0.15f),
        shadowElevation = if (unlocked) 2.dp else 0.dp
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (unlocked) "✅" else "🔒", fontSize = 26.sp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.SemiBold,
                    color = if (unlocked) NightForest else EarthBrown, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = EarthBrown)
            }
            if (unlocked) {
                Text("New!", fontSize = 11.sp, color = LeafGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
