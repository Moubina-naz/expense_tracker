package com.example.expensetracker

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.expensetracker.Room.WeeklyData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
suspend fun getLast5WeeksWithSums(
    currentDate: String,
    getSumForWeek: suspend (startDate: String, endDate: String) -> Double
): List<WeeklyData> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = LocalDate.parse(currentDate, formatter)
    val weekFields = WeekFields.of(Locale.getDefault())

    return (0..4).map { weekOffset ->
        val weekStart = today
            .with(weekFields.dayOfWeek(), 1L) // Monday
            .minusWeeks(weekOffset.toLong())
        val weekEnd = weekStart.plusDays(6) // Sunday

        val dbFormatStart = weekStart.format(formatter)
        val dbFormatEnd = weekEnd.format(formatter)

        val total = getSumForWeek(dbFormatStart, dbFormatEnd)

        WeeklyData(
            weekStart = dbFormatStart,
            weekEnd = dbFormatEnd,
            total = total,
            label = when (weekOffset) {
                0 -> "Current Week"
                else -> formatDateRange(weekStart, weekEnd)
            }
        )
    }.sortedBy{ LocalDate.parse(it.weekStart, formatter) } // Fix: Sort by date descending
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateRange(start: LocalDate, end: LocalDate): String {
    val monthFormat = DateTimeFormatter.ofPattern("MMM", Locale.getDefault())
    return if (start.month == end.month) {
        "${start.dayOfMonth}-${end.dayOfMonth} ${start.format(monthFormat)}"
    } else {
        "${start.dayOfMonth} ${start.format(monthFormat)} - ${end.dayOfMonth} ${end.format(monthFormat)}"
    }
}