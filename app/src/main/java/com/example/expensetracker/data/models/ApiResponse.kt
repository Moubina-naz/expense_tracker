package com.example.expensetracker.data.models


data class RegisterRequest(
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val password: String
)

data class UserResponse(
    val id: Int,
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val base_currency: String
)

data class UserUpdateRequest(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val base_currency: String? = null
)

data class PasswordChangeRequest(
    val old_password: String,
    val new_password: String
)

data class MessageResponse(
    val message: String
)

// ✅ TRANSACTION MODELS (CORRECTED FOR YOUR API STRUCTURE)
data class TransactionRequest(
    val title: String,
    val original_amount: Double,
    val original_currency: String,
    val date: String, // Format: "20/09/2025"
    val category: String
)

data class TransactionResponse(
    val id: Long,
    val user: Int,
    val title: String,
    val original_amount: Double,
    val original_currency: String,
    val base_amount: Double,
    val base_currency: String,
    val date: String, // Format: "20/09/2025" - Your serializer converts it!
    val category: String,
    val created_at: String
)
data class TokenRequest(
    val username: String,
    val password: String
)

data class TokenResponse(
    val access: String,      // Access token
    val refresh: String      // Refresh token
)