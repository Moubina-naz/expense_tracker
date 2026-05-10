package com.example.expensetracker.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.models.UserPreferences
import com.example.expensetracker.utils.CurrencyManager
import com.example.expensetracker.data.Room.Graph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    private val _userName = MutableStateFlow(userPreferences.getUserName())
    val userName: StateFlow<String> = _userName
    
    private val _currency = MutableStateFlow(userPreferences.getUserCurrency())
    val currency: StateFlow<String> = _currency

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState
    
    fun updateCurrency(newCurrency: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val oldCurrency = userPreferences.getUserCurrency()
                if (oldCurrency != newCurrency) {
                    // Try to fetch latest rates first
                    CurrencyManager.fetchLatestRates()
                    
                    val rate = CurrencyManager.convert(1.0, oldCurrency, newCurrency)
                    Graph.transactionRepository.convertCurrencyInDb(rate)
                    
                    // Save the new currency
                    val name = userPreferences.getUserName()
                    val age = "" // Keep existing or handle properly
                    val country = "" // Keep existing or handle properly
                    userPreferences.saveUserInfo(name, age, country, newCurrency)
                    
                    _currency.value = newCurrency
                }
                _updateState.value = UpdateState.Success
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating currency: ${e.message}")
                _updateState.value = UpdateState.Error
            }
        }
    }

    fun updateProfileInfo(name: String, age: String, country: String, currency: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val oldCurrency = userPreferences.getUserCurrency()
                if (oldCurrency != currency) {
                    CurrencyManager.fetchLatestRates()
                    val rate = CurrencyManager.convert(1.0, oldCurrency, currency)
                    Graph.transactionRepository.convertCurrencyInDb(rate)
                }

                userPreferences.saveUserInfo(name, age, country, currency)
                _userName.value = name
                _currency.value = currency
                _updateState.value = UpdateState.Success
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error
            }
        }
    }
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    object Error : UpdateState()
}
