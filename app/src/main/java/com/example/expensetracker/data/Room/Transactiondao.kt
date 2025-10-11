package com.example.expensetracker.data.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.example.expensetracker.data.models.BudgetEntity
import com.example.expensetracker.data.models.BudgetStatus
import com.example.expensetracker.data.models.CategoryTotal
import com.example.expensetracker.data.models.DailyData
import com.example.expensetracker.data.models.MonthlyData
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.data.models.WeeklyData
import kotlinx.coroutines.flow.Flow

@Dao
interface  TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id ")
    abstract fun getTransactionById(id: Long): Flow<TransactionEntity>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 6")
    abstract fun getRecentTransactions(): Flow<List<TransactionEntity>>

    @Query("""
    SELECT * FROM transactions 
    WHERE LOWER(title) LIKE LOWER(:query) 
       OR LOWER(category) LIKE LOWER(:query)
       OR CAST(amount AS TEXT) LIKE :query
       OR LOWER(title) LIKE LOWER('%' || :query || '%')
       OR LOWER(category) LIKE LOWER('%' || :query || '%')
    ORDER BY date DESC
""")
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Query(
        """
    SELECT category, SUM(amount) as total 
    FROM transactions 
    WHERE 
        (substr(date, 4, 2) = :month AND substr(date, 7, 4) = :year) OR  -- dd/MM/yyyy format
        (substr(date, 6, 2) = :month AND substr(date, 1, 4) = :year)       -- yyyy/MM/dd format
    GROUP BY category
"""
    )
    fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>>


    @Query(
        """
    SELECT 
        MIN(date) as weekStart,
        MAX(date) as weekEnd,
        SUM(amount) as total
    FROM transactions
    WHERE date >= :startDate  -- Now actually using the parameter
    GROUP BY substr(date, 7, 4) || substr(date, 4, 2) || (substr(date, 1, 2)/7)
    ORDER BY weekStart DESC
    LIMIT 5
"""
    )
    fun getWeeklyTotals(startDate: String): Flow<List<WeeklyData>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
     SELECT 
        substr(date, 4, 2) || '/' || substr(date, 7, 4) as monthYear,
        SUM(amount) as totalExpenses
    FROM transactions
    WHERE date IS NOT NULL 
      AND date != ''
      AND date LIKE '__/__/____'  -- Ensures dd/MM/yyyy format
    GROUP BY monthYear
    ORDER BY monthYear DESC
    LIMIT 12
"""
    )
    fun getMonthlyTotals(): Flow<List<MonthlyData>>


    @Query(
        """
    SELECT 
        date,
        SUM(amount) as total
    FROM transactions
    WHERE date BETWEEN :start AND :end
    GROUP BY date
    ORDER BY date
"""
    )
    fun getDailyTotals(start: String, end: String): Flow<List<DailyData>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0) 
        FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
    """
    )
    suspend fun getSumBetweenDates(startDate: String, endDate: String): Double

    //BUDGET.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets WHERE monthYear = :monthYear LIMIT 1")
    suspend fun getBudgetForMonth(monthYear: String): BudgetEntity?

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudget(id: Long)

    @Query("""
    SELECT 
        (SELECT amount FROM budgets 
         WHERE monthYear = :monthYear 
         ORDER BY createdAt DESC LIMIT 1) AS budget,
        
        (SELECT SUM(amount) FROM transactions 
         WHERE substr(date, 4, 2) || '/' || substr(date, 7, 4) = :monthYear) AS spent
    """)
    suspend fun getBudgetStatus(monthYear: String): BudgetStatus

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Query("""
        SELECT * FROM transactions 
        WHERE 
            (substr(date, 4, 2) = :month AND substr(date, 7, 4) = :year) OR  -- dd/MM/yyyy format
            (substr(date, 6, 2) = :month AND substr(date, 1, 4) = :year)     -- yyyy/MM/dd format
        ORDER BY date DESC
    """)
    fun getTransactionsByMonth(month: String, year: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getTransactionsByWeek(startDate: String, endDate: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE category = :category 
            AND (
                (substr(date, 4, 2) = :month AND substr(date, 7, 4) = :year) OR  -- dd/MM/yyyy format
                (substr(date, 6, 2) = :month AND substr(date, 1, 4) = :year)     -- yyyy/MM/dd format
            )
        ORDER BY date DESC
    """)
    fun getCategoryTransactionsByMonth(category: String, month: String, year: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) 
        FROM transactions 
        WHERE category = :category 
            AND (
                (substr(date, 4, 2) = :month AND substr(date, 7, 4) = :year) OR
                (substr(date, 6, 2) = :month AND substr(date, 1, 4) = :year)
            )
    """)
    suspend fun getCategoryTotalByMonth(category: String, month: String, year: String): Double


    @Query("""
        SELECT * FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE category = :category
        ORDER BY date DESC
    """)
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>
}








