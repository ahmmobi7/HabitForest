package com.habitforest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.habitforest.domain.usecase.RewardEvent
import com.habitforest.ui.theme.*

@Composable
fun RewardOverlay(event: RewardEvent, onDismiss: () -> Unit) {
    val scaleAnim by animateFloatAsState(
        targetValue  = 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "rewardScale"
    )
    // Confetti pulse
    val pulse by rememberInfiniteTransition(label = "").animateFloat(
        1f, 1.08f,
        infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim),
            shape           = RoundedCornerShape(28.dp),
            color           = DarkMoss,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("🎉", fontSize = (64 * pulse).sp)

                Text(
                    "LEVEL UP!",
                    fontSize   = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = GoldenYes
                )

                Text(
                    event.message,
                    fontSize   = 16.sp,
                    color      = SkyBlue,
                    textAlign  = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick  = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape  = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenYes)
                ) {
                    Text(
                        "Claim Reward 🏆",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 17.sp,
                        color      = NightForest
                    )
                }
            }
        }
    }
}
