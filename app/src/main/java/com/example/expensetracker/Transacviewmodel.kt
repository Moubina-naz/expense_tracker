package com.example.expensetracker

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.Room.CategoryItem
import com.example.expensetracker.Room.CategoryTotal
import com.example.expensetracker.Room.Graph
import com.example.expensetracker.Room.MonthItem
import com.example.expensetracker.Room.MonthlyData
import com.example.expensetracker.Room.MonthlySummary

import com.example.expensetracker.Room.TransactionEntity
import com.example.expensetracker.Room.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Transacviewmodel(
    private val repository : TransactionRepository = Graph.TransactionRepository)
    : ViewModel() {

    var transacTitlestate by mutableStateOf("")
    var transacAmountstate by mutableStateOf("")
    var transacDatestate by mutableStateOf(getCurrentDate())
    var transacIconstate by mutableStateOf(0)
    var selectedCategory by mutableStateOf<CategoryItem?>(null)
    var currentEditingId by mutableStateOf<Long?>(null) // Track which transaction we're editing


    fun onTransacTitleChange(newTitle: String) {
        transacTitlestate = newTitle
    }

    fun onTransacAmountChange(newAmount: String) {
        transacAmountstate = newAmount
    }

    fun onTransacDateChange(newDate: String) {
        transacDatestate = newDate
    }

    fun onTransacIconChange(newIcon: Int) {
        transacIconstate = newIcon
    }

    val transactionList = repository.getTransaction()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun resetForNewTransaction() {
        currentEditingId = null
        transacTitlestate = ""
        transacAmountstate = ""
        transacDatestate = getCurrentDate()
        transacIconstate = 0
        selectedCategory = null
    }


    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }


    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    val recentTransactions = repository.getRecentTransactions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadTransactionForEditing(transaction: TransactionEntity) {
        currentEditingId = transaction.id
        transacTitlestate = transaction.title
        transacAmountstate = transaction.amount.toString()
        transacDatestate = transaction.date
        transacIconstate = transaction.icon
        selectedCategory = categories.find { it.iconRes == transaction.icon }
    }

    fun clearFields() {
        currentEditingId = null
        transacTitlestate = ""
        transacAmountstate =""
        transacDatestate = getCurrentDate()
        transacIconstate = 0
        selectedCategory = null
    }


    fun onCategorySelected(category: CategoryItem) {
        selectedCategory = category
        transacIconstate = category.iconRes
    }

    fun getTransacById(id: Long): TransactionEntity? {
        return transactionList.value.find { it.id == id }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    val categories = listOf(
        CategoryItem("Food", R.drawable.takeout),
        CategoryItem("Shopping", R.drawable.shopping),
        CategoryItem("Travel", R.drawable.travel),
        CategoryItem("Bills", R.drawable.bills),
        CategoryItem("Groceries", R.drawable.grocery),
        CategoryItem("Entertainment", R.drawable.entertaintment),
        CategoryItem("Transport", R.drawable.transport)
    )

    //SEARCH
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    val filteredTransactions: StateFlow<List<TransactionEntity>> =
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                _isSearching.value = true
                if (query.isBlank()) {
                    repository.getTransaction()
                } else {
                    val q = "%${query.lowercase()}%"
                    repository.searchTransactions(q)
                        .catch { emit(emptyList()) }
                        .onCompletion { _isSearching.value = false }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
        _isSearching.value = newQuery.isNotBlank()
    }

    //STATS


    private fun getCurrentMonthYear(): String {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        return "$month/$year"
    }

    private val _selectedMonth = MutableStateFlow(getCurrentMonthYear())
    val selectedMonth: StateFlow<String> = _selectedMonth

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categoryTotals = MutableStateFlow<List<CategoryTotal>>(emptyList())
    val categoryTotals: StateFlow<List<CategoryTotal>> = _categoryTotals

    init {
        loadCategoryData()
    }

    fun selectMonth(month: String, year: String) {
        val formattedMonth = String.format("%02d", month.toInt())
        _selectedMonth.value = "$formattedMonth/$year"
        loadCategoryData()
    }
    private fun loadData() {
        viewModelScope.launch {
            repository.getCategoryTotals(selectedMonth.value.split("/")[0],
                selectedMonth.value.split("/")[1])
                .first() // 👈 Only take the first emission
                .let { _categoryTotals.value = it }
        }
    }
    private fun loadCategoryData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val (month, year) = _selectedMonth.value.split("/")

                // KEY FIX: Use .first() to get only one emission
                val totals = withTimeout(5000) {
                    repository.getCategoryTotals(month, year).first()
                }
                _categoryTotals.value = totals

            } catch (e: Exception) {
                println("Error: ${e.message}")
                _categoryTotals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generatePastMonths(count: Int = 6): List<MonthItem> {
        val current = YearMonth.now()
        return (0 until count).map { offset ->
            val date = current.minusMonths(offset.toLong())
            MonthItem(
                label = "${date.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())} ${date.year}",
                value = date.format(DateTimeFormatter.ofPattern("MM/yyyy")))
        }.reversed()
    }

}

