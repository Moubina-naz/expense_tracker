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
import kotlinx.coroutines.flow.SharingStarted
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

    /*lateinit var getAllTransactions: Flow<List<TransactionEntity>>
      init {
          viewModelScope.launch {
              getAllTransactions = repository.getTransaction()
          }
      }

     */
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
        viewModelScope.launch(Dispatchers.IO)  {

            repository.addTransaction(transaction)
            /*     val transaction = TransactionEntity(
            id = currentEditingId ?: 0,
            title = transacTitlestate,
            amount = transacAmountstate,
            date = transacDatestate,
            icon = selectedCategory?.iconRes ?: 0
        )
        if (currentEditingId != null) {
            repository.updateTransaction(transaction)
        } else {
            repository.addTransaction(transaction)
        }
        resetForNewTransaction()
    }

    */
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


    fun onCategorySelected(category: CategoryItem){
      selectedCategory=category
      transacIconstate=category.iconRes
  }

    fun getTransacById(id:Long): TransactionEntity? {
        return transactionList.value.find { it.id == id }
    }

    fun getCurrentDate(): String{
        val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
}