package com.example.expensetracker.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.Room.TransactionDao
import com.example.expensetracker.Room.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)

abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

}

  /*  companion object {
        @Volatile
        private var INSTANCE: TransactionsDatabase? = null

        fun getDatabase(context: Context): TransactionsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionsDatabase::class.java,
                    "expense_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

   */

