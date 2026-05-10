package com.example.expensetracker.data.Room

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.expensetracker.data.models.BudgetEntity
import com.example.expensetracker.data.models.BudgetStatus
import com.example.expensetracker.data.models.CategoryTotal
import com.example.expensetracker.data.models.MonthlyData
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.data.models.WeeklyData
import com.example.expensetracker.data.models.getLast5WeeksWithSums
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class TransactionRepository(private val dao: TransactionDao) {
    val allTrans: Flow<List<TransactionEntity>> = dao.getAllTransactions()

    // SIMPLE VERSION - NO SYNC MANAGER
    suspend fun addTransaction(transaction: TransactionEntity) {
        dao.addTransaction(transaction) // Just local Room
    }

    fun getTransaction(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        dao.deleteTransaction(transaction) // Just local Room
    }

    fun getTransactionById(id: Long): Flow<TransactionEntity> {
        return dao.getTransactionById(id)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        dao.updateTransaction(transaction) // Just local Room
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> = dao.getRecentTransactions()

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> {
        return dao.searchTransactions("%$query%") // Simple Room search
    }

    fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>> {
        return dao.getCategoryTotals(month, year)
    }

    // KEEP ALL YOUR PERFECT ANALYTICS CODE EXACTLY AS IS
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
                    monthName = month.format(formatter)
                )
            }.reversed()
        } else {
            dbData.map { item ->
                val (month, year) = item.monthYear.split('/')
                val ym = YearMonth.of(year.toInt(), month.toInt())
                MonthlyData(
                    monthYear = item.monthYear,
                    totalExpenses = item.totalExpenses,
                    monthName = ym.format(formatter)
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

    // BUDGET - UNCHANGED
    suspend fun setBudget(amount: Double, monthYear: String) {
        Log.d("BUDGET_DEBUG", "Attempting to save: $amount for $monthYear")
        val entity = BudgetEntity(amount = amount, monthYear = monthYear)
        val rowId = dao.insertOrUpdateBudget(entity)
        Log.d("BUDGET_DEBUG", "Insert result: $rowId")

        // Immediately verify what was stored
        val saved = dao.getBudgetForMonth(monthYear)
        Log.d("BUDGET_DEBUG", "Actually stored: ${saved?.amount ?: "null"}")
    }

    suspend fun getBudgetForMonth(monthYear: String): BudgetEntity? {
        return dao.getBudgetForMonth(monthYear)
    }

    suspend fun getBudgetStatus(monthYear: String): BudgetStatus {
        return dao.getBudgetStatus(monthYear)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCurrentMonthBudgetStatus(): BudgetStatus {
        return try {
            val currentMonthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))
            getBudgetStatus(currentMonthYear)
        } catch (e: Exception) {
            BudgetStatus(0.0, 0.0)
        }
    }

    suspend fun getAllBudgets(): List<BudgetEntity> {
        return dao.getAllBudgets()
    }

    fun getTransactionsByMonth(month: String, year: String): Flow<List<TransactionEntity>> {
        return dao.getTransactionsByMonth(month, year)
    }

    fun getTransactionsByWeek(startDate: String, endDate: String): Flow<List<TransactionEntity>> {
        return dao.getTransactionsByWeek(startDate, endDate)
    }

    fun getCategoryTransactionsByMonth(category: String, month: String, year: String): Flow<List<TransactionEntity>> {
        return dao.getCategoryTransactionsByMonth(category, month, year)
    }

    suspend fun getCategoryTotalByMonth(category: String, month: String, year: String): Double {
        return dao.getCategoryTotalByMonth(category, month, year)
    }

    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<TransactionEntity>> {
        return dao.getTransactionsByDateRange(startDate, endDate)
    }

    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>> {
        return dao.getTransactionsByCategory(category)
    }

    suspend fun convertCurrencyInDb(rate: Double) {
        dao.convertAllTransactions(rate)
        dao.convertAllBudgets(rate)
    }
}












