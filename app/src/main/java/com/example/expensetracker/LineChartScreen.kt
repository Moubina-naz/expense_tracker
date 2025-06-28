package com.example.expensetracker

import android.graphics.Color.toArgb
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.EmptyBuildDrawCacheParams.density
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.Room.TransactionDao
import com.example.expensetracker.Room.TransactionEntity
import com.example.expensetracker.Room.WeeklyData
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.moveTo


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChartScreen(
viewmodel: Transacviewmodel
){
    var chartType by remember { mutableStateOf<ChartType>(ChartType.Weekly) }

    // Initialize data
    LaunchedEffect(Unit) {
        viewmodel.initDataIfNeeded()
    }

    // Load data when chart type changes
    LaunchedEffect(chartType) {
        when (chartType) {
            ChartType.Weekly -> viewmodel.loadWeeklyData()
            ChartType.Monthly -> viewmodel.loadMonthlyData()
        }
    }

    // Collect data states
    val weeklyData by viewmodel.weeklyData.collectAsState()
    val monthlyData by viewmodel.monthlyData.collectAsState()
    val dailyData by viewmodel.dailyData.collectAsState()
    // Initialize data if needed

    Column(modifier = Modifier.padding(16.dp)) {
        ChartSelector(
            currentSelection = chartType,
            onSelectionChanged = { newType ->
                chartType = newType
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic title based on selection
        Text(
            text = when (chartType) {
                //ChartType.Daily -> "Daily Expenses (Last 30 days)"
                ChartType.Weekly -> "Weekly Expenses"
                ChartType.Monthly -> "Monthly Expenses (Last 12 months)"
            },
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (chartType) {
            ChartType.Weekly -> {
                if (weeklyData.isNotEmpty()) {
                    SimpleLineChart(
                        amounts = weeklyData.map { it.total.toFloat() },
                        labels = weeklyData.map { it.label },
                        modifier = Modifier
                    )
                } else {
                    EmptyChartPlaceholder()
                }
            }

            ChartType.Monthly -> {
                if (monthlyData.isNotEmpty()) {
                    SimpleLineChart(
                        amounts = monthlyData.map { it.totalExpenses.toFloat() },
                        labels = monthlyData.map { it.monthName }, // Use formatted "Jun'25" labels
                        modifier = Modifier
                    )
                } else {
                    EmptyChartPlaceholder()
                }
            }

            /*ChartType.Daily -> {
                if (dailyData.isNotEmpty()) {
                    SimpleLineChart(
                        amounts = dailyData.map { it.total.toFloat() },
                        labels =dailyData.map { it.dayName }, // Shows "MM-dd"
                        modifier = Modifier
                    )
                } else {
                    EmptyChartPlaceholder()
                }
            }

             */
        }
    }
}
@Composable
public fun EmptyChartPlaceholder() {
    Box(
        modifier = Modifier, // Reuses the same modifier as actual charts
        contentAlignment = Center // Centers content both vertically and horizontally
    ) {
        Text(
            text = "No data available",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            // Uses theme color for better visibility on the surface
        )
    }
}
@Composable
fun ChartSelector(
    currentSelection: ChartType,
    onSelectionChanged: (ChartType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = when(currentSelection) {
                    //is ChartType.Daily -> "Daily"
                    is ChartType.Weekly -> "Weekly"
                    is ChartType.Monthly -> "Monthly"
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Weekly") },
                onClick = {
                    onSelectionChanged(ChartType.Weekly)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Monthly") },
                onClick = {
                    onSelectionChanged(ChartType.Monthly)
                    expanded = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateWeeklyData(transactions: List<TransactionEntity>): List<WeeklyData> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return transactions
        .groupBy {
            val date = LocalDate.parse(it.date, formatter)
            date.with(java.time.DayOfWeek.MONDAY)
        }
        .toSortedMap()
        .map { (weekStart, transactionsInWeek) ->
            val total = transactionsInWeek.sumOf { it.amount }
            val weekEnd = weekStart.plusDays(6)
            WeeklyData(
                weekStart = weekStart.format(formatter),
                weekEnd = weekEnd.format(formatter),
                total = total,
                label = "${weekStart.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${weekStart.dayOfMonth}-${weekEnd.dayOfMonth}"
            )
        }
}