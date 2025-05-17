package com.example.expensetracker.Room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.TextStyle
import com.example.expensetracker.DataPoint
import com.example.expensetracker.TrendRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.util.Locale
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.temporal.WeekFields


class TransactionRepository(private val dao: TransactionDao) {
    val allTrans: Flow<List<TransactionEntity>> = dao.getAllTransactions()


    suspend fun addTransaction(transaction: TransactionEntity) {
        dao.addTransaction(transaction)
    }

    fun getTransaction(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        dao.deleteTransaction(transaction)
    }

    fun getTransactionById(id: Long): Flow<TransactionEntity> {
        return dao.getTransactionById(id)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        dao.updateTransaction(transaction)
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> = dao.getRecentTransactions()

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> {
        return dao.searchTransactions("%$query%")
    }

fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>> {
        return dao.getCategoryTotals(month, year)
    }

    //fun getMonthlySummaries(): Flow<List<MonthlySummary>> { return dao.getMonthlySummaries()}
    fun getMonthlyData(): Flow<List<MonthlyData>> {
        return dao.getMonthlyData()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTrendData(range: TrendRange): Flow<List<DataPoint>> {
        val (startDate, endDate) = calculateDateRange(range)

        return when (range) {
            TrendRange.WEEKLY -> dao.getDailyTotals(startDate.toString(), endDate.toString())
                .map { dailyTotals ->
                    val allDates = generateDateRange(startDate, endDate)
                    allDates.map { date ->
                        val total = dailyTotals.find { it.date == date }?.total ?: 0f
                        DataPoint(
                            label = "", // handled in UI (e.g., dayOfWeek),
                            value = total,
                            date = date
                        )
                    }
                }

            TrendRange.MONTHLY -> dao.getWeeklyTotals(startDate.toString())
                .map { weeklyTotals ->
                    val allWeeks = generateWeeklyStartDates(startDate, endDate)
                    allWeeks.mapIndexed { index, weekStart ->
                        val total = weeklyTotals.find { it.startDate == weekStart }?.total ?: 0f
                        DataPoint(
                            label = "", // handled in UI: "Week ${index + 1}"
                            value = total,
                            date = weekStart
                        )
                    }
                }

            TrendRange.ANNUAL -> dao.getMonthlyTotals(startDate.toString())
                .map { monthlyTotals ->
                    val allMonths = generateMonthlyStartDates(startDate, endDate)
                    allMonths.map { monthStart ->
                        val total = monthlyTotals.find { it.startDate == monthStart }?.total ?: 0f
                        DataPoint(
                            label = "", // handled in UI: Jan, Feb...
                            value = total,
                            date = monthStart
                        )
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDateRange(range: TrendRange): Pair<LocalDate, LocalDate> {
        val endDate = LocalDate.now()
        val startDate = when (range) {
            TrendRange.WEEKLY -> endDate.with(DayOfWeek.MONDAY)
            TrendRange.MONTHLY -> endDate.minusWeeks(4).with(DayOfWeek.MONDAY)
            TrendRange.ANNUAL -> endDate.minusMonths(12).withDayOfMonth(1)
        }
        return Pair(startDate, endDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateDateRange(start: LocalDate, end: LocalDate): List<LocalDate> {
        return generateSequence(start) { it.plusDays(1) }
            .takeWhile { it <= end }
            .toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateWeeklyStartDates(start: LocalDate, end: LocalDate): List<LocalDate> {
        return generateSequence(start) { it.plusWeeks(1) }
            .takeWhile { it <= end }
            .toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateMonthlyStartDates(start: LocalDate, end: LocalDate): List<LocalDate> {
        return generateSequence(start) { it.plusMonths(1) }
            .takeWhile { it <= end }
            .toList()
    }
}









