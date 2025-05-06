package com.example.expensetracker.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.Room.TransactionDao
import com.example.expensetracker.Room.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 2)

abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add any schema changes here
                // Example if you added a new column:
                // database.execSQL("ALTER TABLE transactions ADD COLUMN new_column TEXT DEFAULT NULL")
            }
        }


    }}

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

