package com.buildstack.newsflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glass(
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.6f),
                Color.Black.copy(alpha = 0.3f)
            )
        )
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                Color.White.copy(alpha = 0.1f)
            )
        ),
        shape = shape
    )
