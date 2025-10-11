package com.example.expensetracker.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Room.Graph
import com.example.expensetracker.data.Room.TransactionRepository
import com.example.expensetracker.data.models.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnalyticsViewModel(
    private val repository: TransactionRepository = Graph.transactionRepository
) : ViewModel() {

    // State for analytics results
    private val _analyticsResults = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val analyticsResults: StateFlow<List<TransactionEntity>> = _analyticsResults

    private val _analyticsLoading = MutableStateFlow(false)
    val analyticsLoading: StateFlow<Boolean> = _analyticsLoading

    private val _analyticsSummary = MutableStateFlow<AnalyticsSummary?>(null)
    val analyticsSummary: StateFlow<AnalyticsSummary?> = _analyticsSummary

    // Current filter state
    private val _currentFilter = MutableStateFlow<AnalyticsFilter?>(null)
    val currentFilter: StateFlow<AnalyticsFilter?> = _currentFilter

    // 1. Get transactions for a specific month
    fun getTransactionsByMonth(month: String, year: String): Flow<List<TransactionEntity>> {
        return repository.getTransactionsByMonth(month, year)
    }

    // 2. Get transactions for a specific week
    fun getTransactionsByWeek(startDate: String, endDate: String): Flow<List<TransactionEntity>> {
        return repository.getTransactionsByWeek(startDate, endDate)
    }

    // 3. Get transactions for a specific category in a month
    fun getCategoryTransactionsByMonth(category: String, month: String, year: String): Flow<List<TransactionEntity>> {
        return repository.getCategoryTransactionsByMonth(category, month, year)
    }

    // 4. Get total amount for a category in a month
    suspend fun getCategoryTotalByMonth(category: String, month: String, year: String): Double {
        return repository.getCategoryTotalByMonth(category, month, year)
    }

    // 5. Get transactions by date range
    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<TransactionEntity>> {
        return repository.getTransactionsByDateRange(startDate, endDate)
    }

    // 6. Get transactions by category only
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>> {
        return repository.getTransactionsByCategory(category)
    }

    // 7. Load analytics data with filter
    fun loadAnalyticsData(filter: AnalyticsFilter) {
        viewModelScope.launch {
            _analyticsLoading.value = true
            _currentFilter.value = filter

            try {
                val results = when (filter.type) {
                    AnalyticsType.MONTH -> getTransactionsByMonth(filter.param1, filter.param2).first()
                    AnalyticsType.WEEK -> getTransactionsByWeek(filter.param1, filter.param2).first()
                    AnalyticsType.CATEGORY_MONTH -> getCategoryTransactionsByMonth(filter.param1, filter.param2, filter.param2).first()
                    AnalyticsType.CATEGORY -> getTransactionsByCategory(filter.param1).first()
                    AnalyticsType.DATE_RANGE -> getTransactionsByDateRange(filter.param1, filter.param2).first()
                    AnalyticsType.ALL -> repository.getTransaction().first()
                }

                _analyticsResults.value = results
                _analyticsSummary.value = calculateAnalyticsSummary(results)

            } catch (e: Exception) {
                println("❌ Analytics error: ${e.message}")
                _analyticsResults.value = emptyList()
                _analyticsSummary.value = null
            } finally {
                _analyticsLoading.value = false
            }
        }
    }

    // 8. Calculate analytics summary
    private suspend fun calculateAnalyticsSummary(transactions: List<TransactionEntity>): AnalyticsSummary {
        val totalAmount = transactions.sumOf { it.amount }
        val averageAmount = if (transactions.isNotEmpty()) totalAmount / transactions.size else 0.0
        val categoryBreakdown = transactions.groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

        return AnalyticsSummary(
            totalTransactions = transactions.size,
            totalAmount = totalAmount,
            averageAmount = averageAmount,
            categoryBreakdown = categoryBreakdown,
            startDate = transactions.minByOrNull { it.date }?.date ?: "",
            endDate = transactions.maxByOrNull { it.date }?.date ?: ""
        )
    }

    // 9. Clear analytics data
    fun clearAnalytics() {
        _analyticsResults.value = emptyList()
        _analyticsSummary.value = null
        _currentFilter.value = null
    }

    // 10. Get current month/year for default analytics
    private fun getCurrentMonthYear(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR).toString()
        return Pair(month, year)
    }

    // Get week range using Calendar (no API requirement)
    fun getCurrentWeekRange(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Set to Monday of current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeek = formatDate(calendar.time)

        // Set to Sunday of current week
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = formatDate(calendar.time)

        return Pair(startOfWeek, endOfWeek)
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}

// Data classes for analytics
data class AnalyticsSummary(
    val totalTransactions: Int,
    val totalAmount: Double,
    val averageAmount: Double,
    val categoryBreakdown: Map<String, Double>,
    val startDate: String,
    val endDate: String
)

data class AnalyticsFilter(
    val type: AnalyticsType,
    val param1: String = "",
    val param2: String = ""
)

enum class AnalyticsType {
    MONTH, WEEK, CATEGORY_MONTH, CATEGORY, DATE_RANGE, ALL
}