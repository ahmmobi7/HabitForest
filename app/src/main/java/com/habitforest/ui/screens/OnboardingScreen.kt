package com.habitforest.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitforest.ui.theme.*
import com.habitforest.ui.viewmodel.AuthState
import com.habitforest.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

private data class Page(val emoji: String, val title: String, val body: String)

private val PAGES = listOf(
    Page("🌱", "Plant Your Habits",  "Each habit you create becomes a seed in your personal forest."),
    Page("🌳", "Watch Them Grow",    "Check in daily and watch seeds grow from Sapling to Legendary!"),
    Page("🏆", "Earn Rewards",       "Unlock animals, new lands, and visual upgrades as your forest expands.")
)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {
    val pager = rememberPagerState { PAGES.size }
    val scope = rememberCoroutineScope()
    val authState by vm.state.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) onFinish()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0D2B1D), DarkMoss, NightForest)))
    ) {
        // Pages
        HorizontalPager(state = pager, modifier = Modifier.fillMaxSize()) { idx ->
            PageContent(PAGES[idx])
        }

        // Dot indicators
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PAGES.indices.forEach { i ->
                val isActive = pager.currentPage == i
                val width by animateDpAsState(if (isActive) 28.dp else 8.dp, label = "dot")
                val color by animateColorAsState(
                    if (isActive) LeafGreen else LeafGreen.copy(alpha = 0.35f), label = "dotColor"
                )
                Box(
                    Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        // CTA button
        val isLast = pager.currentPage == PAGES.lastIndex
        Button(
            onClick = {
                if (isLast) {
                    vm.signInAnonymously()
                } else {
                    scope.launch { pager.animateScrollToPage(pager.currentPage + 1) }
                }
            },
            enabled = authState !is AuthState.Loading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .fillMaxWidth(0.72f)
                .height(58.dp),
            shape = RoundedCornerShape(29.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LeafGreen)
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NightForest, strokeWidth = 2.dp)
            } else {
                Text(
                    text = if (isLast) "Start Growing 🌱" else "Next →",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NightForest
                )
            }
        }
    }
}

@Composable
private fun PageContent(page: Page) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue  = 1.12f,
        animationSpec = infiniteRepeatable(tween(1400, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "scale"
    )
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(page.emoji, fontSize = (78 * pulse).sp)
        Spacer(Modifier.height(36.dp))
        Text(
            page.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SkyBlue,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Text(
            page.body,
            fontSize = 16.sp,
            color = MossGreen,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
    }
}
