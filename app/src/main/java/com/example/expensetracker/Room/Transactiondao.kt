package com.example.expensetracker.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TransactionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   abstract fun addTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
     abstract fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Update
   abstract fun updateTransaction(transaction: TransactionEntity)

    @Delete
   abstract fun deleteTransaction(transaction: TransactionEntity)

   @Query("SELECT * FROM transactions WHERE id = :id ")
   abstract fun getTransactionById(id: Int): Flow<TransactionEntity>


}