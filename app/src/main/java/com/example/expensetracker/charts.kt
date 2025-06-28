package com.example.expensetracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import java.time.LocalDate


data class ChartModel(
    val value: Float,
    val color: Color,
    val name: String
)
enum class TrendRange(val label:String){
    WEEKLY("This week"),
    MONTHLY("This month"),
    ANNUAL("This year")
}

data class DataPoint(
    val value: Float,
    val date: LocalDate,
    var label: String = "" // Will be generated automatically
)
data class DailyTotal(val date : java.time.LocalDate, val total:Float)
data class WeeklyTotal(val startDate: java.time.LocalDate, val total:Float)
data class MonthlyTotal(val startDate: java.time.LocalDate, val total:Float)


class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)
}
data class GraphAppearance(
    val backgroundColor: Color = Color.White,
    val graphColor: Color = Color.Cyan,
    //val graphAxisColor: Color = Color.Gray,
    val circleColor: Color = Color.Red,
    val isCircleVisible: Boolean = true,
    val isColorAreaUnderChart: Boolean = true,
    val colorAreaUnderChart: Color = Color.Cyan.copy(alpha = 0.3f),
    val graphThickness: Float = 4f
)
sealed class ChartType {
    //data object Daily : ChartType()
    data object Weekly : ChartType()
    data object Monthly : ChartType()
}