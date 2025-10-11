package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CuteProgressBar(
    progress: Float, // 0f..1f
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp) // make it chunky and cute
            .clip(RoundedCornerShape(50)) // fully rounded
            .background(Color(0xFFF1F1F1)) // light gray background
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress) // progress value
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF8BC34A)) // pastel green (or any cute color)
        )
    }
}