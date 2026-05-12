package com.habitforest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.habitforest.ui.theme.*

@Composable
fun GamePanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .border(4.dp, BorderBrown, RoundedCornerShape(12.dp))
            .border(2.dp, GameGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(GameParchment, GameParchmentShadow)
                )
            )
            .padding(12.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    borderColor: Color = BorderBrown,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .border(3.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(GameParchment)
            .padding(8.dp)
    ) {
        Row {
            content()
        }
    }
}
