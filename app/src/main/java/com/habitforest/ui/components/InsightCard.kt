package com.habitforest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.habitforest.domain.usecase.Insight
import com.habitforest.ui.theme.*

@Composable
fun InsightCard(insight: Insight) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        color    = SkyBlue.copy(alpha = 0.55f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(insight.emoji, fontSize = 28.sp)
            Spacer(Modifier.width(12.dp))
            Text(
                insight.message,
                fontSize   = 14.sp,
                color      = NightForest,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            )
        }
    }
}
