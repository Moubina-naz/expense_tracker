package com.example.expensetracker.data.Room

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: TransactionsDatabase
    lateinit var context: Context

    val transactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    fun provide(context: Context) {
        this.context = context
        database = Room.databaseBuilder(
            context,
            TransactionsDatabase::class.java,
            "transaction_db"
        ).fallbackToDestructiveMigration().build()
    }
}