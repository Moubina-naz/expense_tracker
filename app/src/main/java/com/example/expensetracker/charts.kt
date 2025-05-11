package com.example.expensetracker

import androidx.compose.ui.graphics.Color


data class ChartModel(
    val value: Float,
    val color: Color
)

val charts = listOf(
    ChartModel(value = 20f, color =Color.Blue),
    ChartModel(value = 30f, color = Color.Gray),
    ChartModel(value = 40f, color = Color.Green),
    ChartModel(value = 10f, color = Color.Red),
)