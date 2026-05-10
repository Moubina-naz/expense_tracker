package com.example.expensetracker.viewmodels

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.data.models.BudgetStatus
import com.example.expensetracker.data.models.CategoryItem
import com.example.expensetracker.data.models.CategoryTotal
import com.example.expensetracker.data.models.DailyData
import com.example.expensetracker.data.Room.Graph
import com.example.expensetracker.data.models.MonthItem
import com.example.expensetracker.data.models.MonthlyData

import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.data.Room.TransactionRepository
import com.example.expensetracker.data.models.WeeklyData
import com.example.expensetracker.data.models.UserPreferences
import com.example.expensetracker.ui.components.formatDateSlash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale


 open class Transacviewmodel(
    private val repository : TransactionRepository = Graph.transactionRepository)
    : ViewModel() {
     private val userPreferences = UserPreferences(Graph.context)

     var transacTitlestate by mutableStateOf("")
     var transacAmountstate by mutableStateOf("")
     var transacDatestate by mutableStateOf(getCurrentDate())
     var transacIconstate by mutableStateOf(0)
     var selectedCategory by mutableStateOf<CategoryItem?>(null)
     var currentEditingId by mutableStateOf<Long?>(null)

     fun onTransacTitleChange(newTitle: String) {
         transacTitlestate = newTitle
     }

     fun onTransacAmountChange(newAmount: String) {
         transacAmountstate = newAmount
     }

     fun onTransacDateChange(newDate: String) {
         val formattedDate = if (newDate.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
             newDate
         } else {
             formatDateSlash(newDate)
         }
         transacDatestate = formattedDate
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

     fun saveGeminiApiKey(key: String) {
         userPreferences.saveGeminiApiKey(key)
     }

     fun getApiKey(): String? {
         return userPreferences.getGeminiApiKey()
     }

     // REVERT TO OLD SIMPLE VERSION
     fun addTransaction(transaction: TransactionEntity) {
         viewModelScope.launch(Dispatchers.IO) {
             // Simple category handling like old version
             val categoryName = selectedCategory?.name ?: "Other"

             val transactionToAdd = TransactionEntity(
                 title = transacTitlestate,
                 amount = transacAmountstate.toDoubleOrNull() ?: 0.0,
                 date = transacDatestate,
                 icon = selectedCategory?.iconRes ?: 0, // Use category icon directly
                 category = categoryName
             )

             repository.addTransaction(transactionToAdd)
         }
     }

     fun updateTransaction(transaction: TransactionEntity) {
         viewModelScope.launch {
             val categoryName = selectedCategory?.name ?: "Other"
             val transactionToUpdate = transaction.copy(
                 title = transacTitlestate, // Use form field instead of parameter
                 amount = transacAmountstate.toDoubleOrNull() ?: 0.0, // Use form field instead of parameter
                 date = transacDatestate, // Use form field instead of parameter
                 icon = selectedCategory?.iconRes ?: transaction.icon,
                 category = categoryName
             )
             repository.updateTransaction(transactionToUpdate)
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
         transacAmountstate = ""
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

     private val _searchQuery = MutableStateFlow("")
     val searchQuery = _searchQuery.asStateFlow()

     private val _isSearching = MutableStateFlow(false)
     val isSearching = _isSearching.asStateFlow()

     private val _searchResults = MutableStateFlow<List<TransactionEntity>>(emptyList())
     val searchResults: StateFlow<List<TransactionEntity>> = _searchResults.asStateFlow()

     init {

         viewModelScope.launch {
             repository.getTransaction().collect { transactions ->
                 _searchResults.value = transactions
             }
         }


         setupSearch()
     }

     private fun setupSearch() {
         viewModelScope.launch {
             searchQuery
                 .debounce(500) // Increased debounce time
                 .distinctUntilChanged()
                 .collect { query ->
                     performSearch(query)
                 }
         }
     }

     private suspend fun performSearch(query: String) {
         _isSearching.value = true
         println("🔍 PERFORMING SEARCH: '$query'")

         try {
             val results = if (query.isBlank()) {

                 repository.getTransaction().first()
             } else {

                 val searchTerm = "%${query.replace(" ", "%")}%"
                 println("🔍 SEARCH TERM: '$searchTerm'")

                 repository.searchTransactions(searchTerm).first()
             }

             _searchResults.value = results
             println("SEARCH COMPLETE: Found ${results.size} results")

         } catch (e: Exception) {
             println(" SEARCH ERROR: ${e.message}")
             _searchResults.value = emptyList()
         } finally {
             _isSearching.value = false
             println("🏁 SEARCH FINISHED")
         }
     }

     fun updateSearchQuery(newQuery: String) {
         _searchQuery.value = newQuery
     }

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
         val formattedMonth = if (month.length == 1) "0$month" else month
         _selectedMonth.value = "$formattedMonth/$year"
         loadCategoryData()
     }

     private fun loadData() {
         viewModelScope.launch {
             repository.getCategoryTotals(
                 selectedMonth.value.split("/")[0],
                 selectedMonth.value.split("/")[1]
             )
                 .first()
                 .let { _categoryTotals.value = it }
         }
     }

     private fun loadCategoryData() {
         viewModelScope.launch {
             _isLoading.value = true
             try {
                 val (month, year) = _selectedMonth.value.split("/")
                 val totals = withTimeout(5000) {
                     repository.getCategoryTotals(month, year).first()
                 }
                 _categoryTotals.value = totals
             } catch (e: Exception) {
                 println("Error loading category data: ${e.message}")
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
                 label = "${
                     date.month.getDisplayName(
                         TextStyle.SHORT,
                         Locale.getDefault()
                     )
                 } ${date.year}",
                 value = date.format(DateTimeFormatter.ofPattern("MM/yyyy"))
             )
         }.reversed()
     }

     // LINECHARTVIEWMODEL - UNCHANGED
     private val _dailyData = MutableStateFlow<List<DailyData>>(emptyList())
     val dailyData: StateFlow<List<DailyData>> = _dailyData

     private val _monthlyData = MutableStateFlow<List<MonthlyData>>(emptyList())
     val monthlyData: StateFlow<List<MonthlyData>> = _monthlyData

     @RequiresApi(Build.VERSION_CODES.O)
     fun debugMonthlyData() {
         viewModelScope.launch {
             val data = repository.getLast12Months()
             println("MONTHLY DATA VERIFICATION:")
             data.forEach {
                 println("${it.monthName} (${it.monthYear}): ${it.totalExpenses}")
             }
         }
     }

     @RequiresApi(Build.VERSION_CODES.O)
     fun loadMonthlyData() = viewModelScope.launch {
         _monthlyData.value = repository.getLast12Months()
     }

     private val _weeklyData = MutableStateFlow<List<WeeklyData>>(emptyList())
     val weeklyData: StateFlow<List<WeeklyData>> = _weeklyData

     @RequiresApi(Build.VERSION_CODES.O)
     fun loadWeeklyData() = viewModelScope.launch {
         _weeklyData.value = repository.getLast5Weeks()
     }

     fun initDataIfNeeded() {
         if (_dailyData.value.isEmpty() &&
             _weeklyData.value.isEmpty() &&
             _monthlyData.value.isEmpty() &&
             Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
         ) {
             viewModelScope.launch { loadDataSafely() }
         }
     }

     @RequiresApi(Build.VERSION_CODES.O)
     private suspend fun loadDataSafely() {
         loadWeeklyData()
         loadMonthlyData()
     }

     fun debugPrintTransactions() {
         viewModelScope.launch {
             delay(2000)
             repository.getTransaction().collect { transactions ->
                 Log.v("EXPENSE_DEBUG", "=== TRANSACTION DUMP ===")
                 transactions.forEach {
                     Log.v(
                         "EXPENSE_DEBUG",
                         """
                    ID: ${it.id}
                    Title: ${it.title}
                    Amount: ${it.amount}
                    Date: ${it.date}
                    ----------------------
                    """.trimIndent()
                     )
                 }
             }
         }
     }

     fun printData() {
         viewModelScope.launch {
             println("Daily Data: ${dailyData.value}")
             println("Weekly Data: ${weeklyData.value}")
         }
     }

     // BUDGET - UNCHANGED
     private val _budgetStatus = MutableStateFlow(BudgetStatus(0.0, 0.0))
     val budgetStatus: StateFlow<BudgetStatus> = _budgetStatus

     @RequiresApi(Build.VERSION_CODES.O)
     fun saveBudget(amount: Double) {
         viewModelScope.launch {
             val monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))
             repository.setBudget(amount, monthYear)
             loadBudgetStatus()
         }
     }

     @RequiresApi(Build.VERSION_CODES.O)
     fun loadBudgetStatus() {
         viewModelScope.launch {
             val currentMonthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))
             val status = repository.getBudgetStatus(currentMonthYear)
             _budgetStatus.value = status
         }
     }


     fun emergencyClearAllTransactions() {
         viewModelScope.launch(Dispatchers.IO) {
             // Get all transactions and delete them
             val allTransactions = repository.getTransaction().first()
             allTransactions.forEach { transaction ->
                 repository.deleteTransaction(transaction)
             }
             Log.d("CLEANUP", "Deleted ${allTransactions.size} transactions")
         }
     }
 }




