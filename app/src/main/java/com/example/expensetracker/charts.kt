package com.example.expensetracker

import androidx.compose.ui.graphics.Color


data class ChartModel(
    val value: Float,
    val color: Color,
    val name: String
)

val charts = listOf(
    ChartModel(value = 20f, color =Color.Blue, name = cat.category),
    ChartModel(value = 30f, color = Color.Gray, name = cat.category),
    ChartModel(value = 40f, color = Color.Green, name = cat.category),
    ChartModel(value = 10f, color = Color.Red, name = cat.category),
)