// SignupViewModel.kt
package com.example.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Room.Graph
import com.example.expensetracker.data.api.ApiService
import com.example.expensetracker.data.models.RegisterRequest
import com.example.expensetracker.data.models.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val apiService: ApiService = Graph.apiService
) : ViewModel() {

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> = _signupState

    init {
        println("🔄 SignupViewModel created - state: ${_signupState.value}")
    }

    fun signup(username: String, first_name: String, last_name: String, email: String, password: String) {
        println("🎯 Signup called with: $username, $email")

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            println("❌ Validation failed")
            _signupState.value = SignupState.Error("Please fill all fields")
            return
        }

        println("🔄 Starting signup process...")
        _signupState.value = SignupState.Loading

        viewModelScope.launch {
            try {
                println("📤 Making API call...")
                val request = RegisterRequest(
                    email = email,
                    username = username,
                    first_name = first_name,
                    last_name = last_name,
                    password = password
                )

                val response = apiService.register(request)
                println("📥 API response: ${response.code()}")

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        println("✅ Signup successful: ${user.username}")
                        _signupState.value = SignupState.Success(user)
                    } else {
                        println("❌ Signup failed: null response body")
                        _signupState.value = SignupState.Error("Registration failed")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Registration failed"
                    println("❌ API error: $error")
                    _signupState.value = SignupState.Error(error)
                }
            } catch (e: Exception) {
                println("❌ Network error: ${e.message}")
                _signupState.value = SignupState.Error("Network error: ${e.message}")
            }
        }
    }
}

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val user: UserResponse) : SignupState()
    data class Error(val message: String) : SignupState()
}