package com.example.expensetracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.api.RetrofitInstance
import com.example.expensetracker.data.models.RegisterRequest
import com.example.expensetracker.data.models.TokenRequest
import com.example.expensetracker.data.models.UserResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _registerResult = MutableLiveData<Result<UserResponse>>()
    val registerResult: LiveData<Result<UserResponse>> = _registerResult

    private val _profileResult = MutableLiveData<Result<UserResponse>>()
    val profileResult: LiveData<Result<UserResponse>> = _profileResult

    fun registerUser(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.register(
                    RegisterRequest(email, username, firstName, lastName, password)
                )

                if (response.isSuccessful) {
                    _registerResult.value = Result.success(response.body()!!)
                } else {
                    _registerResult.value = Result.failure(
                        Exception("Registration failed: ${response.errorBody()?.string()}")
                    )
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            val resp = RetrofitInstance.api.login(TokenRequest(emailOrUsername, password))
            if (resp.isSuccessful) {
                val tokens = resp.body()!!
                // store in memory + persist
                RetrofitInstance.setAuthToken(tokens.access)
                // persist securely: DataStore / EncryptedSharedPreferences
            } else {
                // handle error
            }
        }
    }

    fun getUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProfile()

                if (response.isSuccessful) {
                    _profileResult.value = Result.success(response.body()!!)
                } else {
                    _profileResult.value = Result.failure(
                        Exception("Failed to get profile: ${response.errorBody()?.string()}")
                    )
                }
            } catch (e: Exception) {
                _profileResult.value = Result.failure(e)
            }
        }
    }
}
