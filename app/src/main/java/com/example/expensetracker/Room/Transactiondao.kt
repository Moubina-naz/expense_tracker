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

   @Query("SELECT * FROM transactions WHERE LOWER(title) LIKE :query OR LOWER(category) LIKE :query")
   abstract fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Query("SELECT Category , SUM(amount) FROM transactions WHERE strftime('%Y', date) = :month AND strftime('%m', date) = :year GROUP BY category")
    abstract fun getMonthlyCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>>


    @Query("SELECT SUM(amount) FROM transactions")
    abstract fun getTotalAmount(): Flow<Double>

    @Query("SELECT strftime('%m%Y', date) as monthYear, SUM(amount) as totalAmount FROM transactions GROUP BY monthYear ORDER BY date")
    abstract fun getMonthlyTotals(): Flow<List<MonthlyData>>
    @Query("SELECT strftime('%m%Y', date) as monthYear, SUM(amount) as totalAmount FROM transactions GROUP BY monthYear ORDER BY date")
    fun getMonthlySummaries(): Flow<List<MonthlySummary>>

}