package com.example.expensetracker.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface  TransactionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
      fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Update
   suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
   suspend fun deleteTransaction(transaction: TransactionEntity)

   @Query("SELECT * FROM transactions WHERE id = :id ")
   abstract fun getTransactionById(id: Long): Flow<TransactionEntity>

   @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 6")
   abstract fun getRecentTransactions(): Flow<List<TransactionEntity>>


}