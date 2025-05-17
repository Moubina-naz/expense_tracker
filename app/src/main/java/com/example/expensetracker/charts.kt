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
    val label: String,     // e.g., "Mon", "Week 2", "Jan"
    val value: Float,      // e.g., 800.0f
    val date: LocalDate    // useful for sorting if needed
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