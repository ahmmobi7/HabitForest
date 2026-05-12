package com.habitforest.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.HomeViewModel

private val ICONS = listOf(
    "🏃", "📚", "💧", "🧘", "💪", "🍎",
    "✍️", "😴", "🎸", "🌿", "💊", "🧹",
    "🛁", "🐾", "🍵", "🧠", "🎯", "📝"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onBack: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state          by vm.state.collectAsState()
    var name           by remember { mutableStateOf("") }
    var selectedIcon   by remember { mutableStateOf("🌱") }
    val keyboard       = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Navigate back when habit is added
    LaunchedEffect(state.toast) {
        if (state.toast?.contains("planted") == true) onBack()
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plant a New Habit 🌱", color = SkyBlue, fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Habit name input
            OutlinedTextField(
                value         = name,
                onValueChange = { if (it.length <= 50) name = it },
                label         = { Text("Habit Name") },
                placeholder   = { Text("e.g. Morning Run, Read 10 Min…") },
                modifier      = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape         = RoundedCornerShape(14.dp),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = LeafGreen,
                    unfocusedBorderColor = MossGreen.copy(alpha = 0.5f),
                    focusedLabelColor    = ForestGreen
                ),
                trailingIcon = {
                    Text("${name.length}/50", fontSize = 11.sp, color = EarthBrown,
                        modifier = Modifier.padding(end = 8.dp))
                }
            )

            // Icon picker
            Text("Choose Icon", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = ForestGreen)
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                items(ICONS) { icon ->
                    val selected = icon == selectedIcon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) LeafGreen.copy(0.25f) else Color.LightGray.copy(0.15f))
                            .border(
                                width  = if (selected) 2.dp else 0.dp,
                                color  = if (selected) LeafGreen else Color.Transparent,
                                shape  = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedIcon = icon },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 24.sp)
                    }
                }
            }

            // Live preview
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = LightSurface,
                border   = BorderStroke(1.dp, LeafGreen.copy(0.4f)),
                shadowElevation = 2.dp
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌱", fontSize = 40.sp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            name.ifBlank { "Your Habit Name" },
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = if (name.isBlank()) EarthBrown else NightForest
                        )
                        Text(
                            "$selectedIcon  Streak: 0 days  ·  Seed 🌱",
                            fontSize = 12.sp,
                            color    = EarthBrown
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Plant button
            Button(
                onClick  = {
                    keyboard?.hide()
                    vm.addNewHabit(name.trim(), selectedIcon)
                },
                enabled  = name.isNotBlank() && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor  = LeafGreen,
                    disabledContainerColor = LeafGreen.copy(alpha = 0.4f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(24.dp),
                        color       = NightForest,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        "Plant Habit 🌱",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = NightForest
                    )
                }
            }
        }
    }
}
