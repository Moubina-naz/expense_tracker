package com.example.expensetracker.viewmodels


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Room.Graph
import com.example.expensetracker.data.api.ApiService
import com.example.expensetracker.data.models.TokenManager
import com.example.expensetracker.data.models.TokenRequest
import com.example.expensetracker.data.models.TokenResponse
import com.example.expensetracker.data.models.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiService: ApiService = Graph.apiService
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Please enter username and password")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val request = TokenRequest(username = username, password = password)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        Graph.setAuthToken(tokenResponse.access)
                        fetchUserProfile(tokenResponse.access)
                    } else {
                        _loginState.value = LoginState.Error("Login failed")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Login failed"
                    _loginState.value = LoginState.Error(error)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Network error: ${e.message}")
            }
        }
    }

    private fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val profileResponse = apiService.getProfile()
                if (profileResponse.isSuccessful) {
                    val user = profileResponse.body()
                    if (user != null) {
                        Graph.setCurrentUser(user)
                        _loginState.value = LoginState.Success(user)
                    } else {
                        _loginState.value = LoginState.Error("Failed to get user profile")
                    }
                } else {
                    _loginState.value = LoginState.Error("Failed to fetch profile")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Profile fetch error: ${e.message}")
            }
        }
    }
}
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}