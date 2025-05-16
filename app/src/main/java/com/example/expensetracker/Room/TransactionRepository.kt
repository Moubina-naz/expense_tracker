package com.example.expensetracker.Room

import com.example.expensetracker.Room.TransactionDao
import com.example.expensetracker.Room.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {
    val allTrans: Flow<List<TransactionEntity>> = dao.getAllTransactions()


    suspend fun addTransaction(transaction: TransactionEntity) {
        dao.addTransaction(transaction)
    }

    fun getTransaction(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        dao.deleteTransaction(transaction)
    }

    fun getTransactionById(id: Long): Flow<TransactionEntity> {
        return dao.getTransactionById(id)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        dao.updateTransaction(transaction)
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> = dao.getRecentTransactions()

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> {
        return dao.searchTransactions("%$query%")
    }

fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>> {
        return dao.getCategoryTotals(month, year)
    }

    //fun getMonthlySummaries(): Flow<List<MonthlySummary>> { return dao.getMonthlySummaries()}
    fun getMonthlyTotals(): Flow<List<MonthlyData>> {
        return dao.getMonthlyTotals()

    }
}


