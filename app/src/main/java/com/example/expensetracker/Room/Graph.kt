package com.example.expensetracker.Room

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: TransactionsDatabase
     val TransactionRepository by lazy {
         TransactionRepository( dao= database.transactionDao())
     }

    fun provide(context:Context){
        database = Room.databaseBuilder(
            context,
            TransactionsDatabase::class.java,"transaction_db"
        )
            .fallbackToDestructiveMigration().
            build()
    }
}