package com.example.expensetracker

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.Room.CategoryItem
import com.example.expensetracker.Room.Graph
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    val transactionList = repository.allTrans
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
        transacAmountstate = transaction.amount
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

    //SEARCH
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
        _isSearching.value = newQuery.isNotBlank()
    }

    val filteredTransactions: StateFlow<List<TransactionEntity>> =
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                _isSearching.value = query.isNotBlank()
                if (query.isBlank()) {
                    repository.allTrans // ✅ RETURNS Flow<List<TransactionEntity>>
                } else {
                    val q = "%${query.lowercase()}%"
                    repository.searchTransactions(q)
                        .catch {
                            emit(emptyList())
                        }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())



}