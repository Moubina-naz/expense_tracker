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
    fun getTransactionById(id: Int): Flow<TransactionEntity> {
        return dao.getTransactionById(id)
    }
    suspend fun updateTransaction(transaction: TransactionEntity) {
        dao.updateTransaction(transaction)
    }



   }


