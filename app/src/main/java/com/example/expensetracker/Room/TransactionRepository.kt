package com.example.expensetracker.Room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.TextStyle
import com.example.expensetracker.getLast5WeeksWithSums
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.time.format.DateTimeFormatter
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



    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    // DAILY (SQL-powered)


//linechart montjly
/*@RequiresApi(Build.VERSION_CODES.O)
suspend fun getLast30Days(): List<DailyData> {
    val endDate = LocalDate.now()
    val startDate = endDate.minusDays(9) // 10 days total (including today)

    val dbDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val displayFormatter = DateTimeFormatter.ofPattern("d''MMM")
        .withLocale(Locale.ENGLISH) // Force English month names

    return dao.getDailyTotals(
        startDate.format(dbDateFormatter),
        endDate.format(dbDateFormatter)
    ).first().map { dbData ->
        val date = try {
            LocalDate.parse(dbData.date, dbDateFormatter)
        } catch (e: Exception) {
            LocalDate.now() // Fallback to today if parsing fails
        }

        DailyData(
            date = dbData.date,
            total = dbData.total,
            dayName = date.format(displayFormatter) // Ensures consistent formatting
        )
    }.sortedBy { it.date }
}*/

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getLast12Months(): List<MonthlyData> {
        val current = YearMonth.now()
        val dbData = dao.getMonthlyTotals().first()
        val formatter = DateTimeFormatter.ofPattern("MMM''yy") // "Jun'25" format

        return if (dbData.isEmpty()) {
            (0 until 12).map { i ->
                val month = current.minusMonths(i.toLong())
                MonthlyData(
                    monthYear = "${month.monthValue}/${month.year}",
                    totalExpenses = 0.0,
                    monthName = month.format(formatter) // Guaranteed non-null
                )
            }.reversed()
        } else {
            dbData.map { item ->
                val (month, year) = item.monthYear.split('/')
                val ym = YearMonth.of(year.toInt(), month.toInt())
                MonthlyData(
                    monthYear = item.monthYear,
                    totalExpenses = item.totalExpenses,
                    monthName = ym.format(formatter) // Guaranteed non-null
                )
            }.sortedBy { it.monthYear }
        }
    }
    // WEEKLY (Kotlin-powered for flexibility)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getLast5Weeks(): List<WeeklyData> {
        val currentDate = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return getLast5WeeksWithSums(currentDate) { start, end ->
            dao.getSumBetweenDates(start, end)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(dateString, formatter)
    }

    // MONTHLY (SQL-powered)




}












