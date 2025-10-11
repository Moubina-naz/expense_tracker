package com.example.expensetracker.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.models.BudgetEntity
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.data.models.Converters

@Database(entities = [TransactionEntity::class, BudgetEntity::class], version = 4)
@TypeConverters(Converters::class)
abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }


    }}



