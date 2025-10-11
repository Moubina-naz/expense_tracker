package com.example.expensetracker.data.api

import com.example.expensetracker.data.models.MessageResponse
import com.example.expensetracker.data.models.PasswordChangeRequest
import com.example.expensetracker.data.models.RegisterRequest
import com.example.expensetracker.data.models.TokenRequest
import com.example.expensetracker.data.models.TokenResponse
import com.example.expensetracker.data.models.TransactionRequest
import com.example.expensetracker.data.models.TransactionResponse
import com.example.expensetracker.data.models.UserResponse
import com.example.expensetracker.data.models.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @POST("api/register_user/")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>
    @POST("api/token/")
    suspend fun login(@Body req: TokenRequest): Response<TokenResponse>
    @GET("api/user/profile/")
    suspend fun getProfile(): Response<UserResponse>
    @PATCH("api/user/profile/")
    suspend fun updateProfile(
        @Body request: UserUpdateRequest
    ): Response<UserResponse>

    @POST("api/user/change-password/")
    suspend fun changePassword(
        @Body request: PasswordChangeRequest
    ): Response<MessageResponse>

    @DELETE("api/user/delete-account/")
    suspend fun deleteAccount(): Response<MessageResponse>


}
