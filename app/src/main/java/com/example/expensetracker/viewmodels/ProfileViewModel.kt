package com.example.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.example.expensetracker.data.api.RetrofitInstance
import com.example.expensetracker.data.models.PasswordChange
import com.example.expensetracker.data.models.User
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.models.PasswordChangeRequest
import com.example.expensetracker.data.models.UserResponse
import com.example.expensetracker.data.models.UserUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow<UserResponse?>(null)
    val userProfile: StateFlow<UserResponse?> = _userProfile

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProfile()
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                } else {
                    _errorMessage.value = "Failed to load profile: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun updateProfile(
        email: String,
        username: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val updateRequest = UserUpdateRequest(
                    email = email,
                    first_name = firstName,
                    last_name = lastName
                )

                val response = RetrofitInstance.api.updateProfile(updateRequest)

                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _updateState.value = UpdateState.Success
                    _errorMessage.value = null
                    // Reload profile to get updated data
                    loadUserProfile()
                } else {
                    _updateState.value = UpdateState.Error
                    _errorMessage.value = "Update failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error
                _errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val passwordChange = PasswordChangeRequest(
                    old_password = oldPassword,
                    new_password = newPassword
                )

                val response = RetrofitInstance.api.changePassword(passwordChange)

                if (response.isSuccessful) {
                    _updateState.value = UpdateState.Success
                    _errorMessage.value = "Password changed successfully"
                    // Clear password fields
                    // You might want to navigate back or show success message
                } else {
                    _updateState.value = UpdateState.Error
                    _errorMessage.value = "Password change failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error
                _errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _updateState.value = UpdateState.Idle
    }
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    object Error : UpdateState()
}

