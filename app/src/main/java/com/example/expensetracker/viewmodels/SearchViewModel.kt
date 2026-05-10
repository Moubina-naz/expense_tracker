package com.example.expensetracker.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.models.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {

    /*private val _searchResults = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val searchResults: StateFlow<List<TransactionEntity>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _errorMessage.value = null
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val results = djangoRepo.searchTransactions(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = "Search failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(category: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val results = djangoRepo.getFilteredTransactions(category = category)
                _searchResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = "Filter failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortTransactions(ordering: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val results = djangoRepo.getFilteredTransactions(ordering = ordering)
                _searchResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = "Sort failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }*/
}