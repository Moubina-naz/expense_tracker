package com.example.expensetracker.data.Room

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.api.*

import com.example.expensetracker.data.models.UserResponse

object Graph {
    lateinit var database: TransactionsDatabase
    lateinit var apiService: ApiService


    var currentUser: UserResponse? = null
        private set

    val transactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }


    val djangoFeatureRepository by lazy {
        DjangoFeatureRepository(apiService = apiService)
    }

    fun setAuthToken(token: String?) {
        RetrofitInstance.setAuthToken(token)
    }


    fun setCurrentUser(user: UserResponse?) {
        currentUser = user
    }
    fun getUsername(): String {
        return currentUser?.username ?: "User" // Fallback to "User"
    }
    fun provide(context: Context) {

        database = Room.databaseBuilder(
            context,
            TransactionsDatabase::class.java,
            "transaction_db"
        ).fallbackToDestructiveMigration().build()

        // API service setup (ONLY for auth)
        apiService = RetrofitInstance.api

    }

}