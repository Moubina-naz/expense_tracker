package com.example.expensetracker

import android.content.Context
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


   /* companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(context: Context): TransactionRepository {
            return INSTANCE ?: synchronized(this) {
                val database = TransactionsDatabase.getDatabase(context)
                val dao = database.transactionDao()
                val instance = TransactionRepository(dao)
                INSTANCE = instance
                instance
            }
        }
    }

    */

   }


