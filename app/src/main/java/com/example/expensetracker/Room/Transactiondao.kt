package com.example.expensetracker.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.example.expensetracker.DailyTotal
import com.example.expensetracker.MonthlyTotal
import com.example.expensetracker.WeeklyTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface  TransactionDao {

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

    @Query("""
    SELECT category, SUM(amount) as total 
    FROM transactions 
    WHERE 
        substr(date, 4, 2) = :month AND  /* Extracts MM from dd/MM/yyyy */
        substr(date, 7, 4) = :year       /* Extracts YYYY from dd/MM/yyyy */
    GROUP BY category
""")
    fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>>
    @Query("SELECT strftime('%m%Y', date) as monthYear, SUM(amount) as totalExpenses FROM transactions GROUP BY monthYear ORDER BY date")
    abstract fun getMonthlyData(): Flow<List<MonthlyData>>

    @RewriteQueriesToDropUnusedColumns

        @Query(
            """
        SELECT date, SUM(amount) as total 
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY date 
        ORDER BY date ASC
    """
        )
        fun getDailyTotals(startDate: String, endDate: String): Flow<List<DailyTotal>>
    @RewriteQueriesToDropUnusedColumns

        @Query(
            """
        SELECT 
            strftime('%Y-%W', date) as weekId,
            MIN(date) as startDate,
            SUM(amount) as total
        FROM transactions
        WHERE date >= :startDate
        GROUP BY weekId
        ORDER BY startDate ASC
    """
        )
        fun getWeeklyTotals(startDate: String): Flow<List<WeeklyTotal>>
    @RewriteQueriesToDropUnusedColumns

        @Query(
            """
        SELECT 
            strftime('%Y-%m', date) as monthId,
            MIN(date) as startDate,
            SUM(amount) as total
        FROM transactions
        WHERE date >= :startDate
        GROUP BY monthId
        ORDER BY startDate ASC
    """
        )
        fun getMonthlyTotals(startDate: String): Flow<List<MonthlyTotal>>
    }

