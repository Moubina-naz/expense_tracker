package com.example.expensetracker.data.api

import com.example.expensetracker.R
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.data.models.TransactionRequest
import com.example.expensetracker.data.models.TransactionResponse

class DjangoFeatureRepository(private val apiService: ApiService) {

    /*suspend fun searchTransactions(query: String): List<TransactionEntity> {
        val response = apiService.getTransactions(search = query)

        if (response.isSuccessful) {
            val body = response.body()
            return if (body != null) {
                body.map { apiTransaction ->
                    TransactionEntity(
                        id = apiTransaction.id,
                        title = apiTransaction.title,
                        amount = apiTransaction.base_amount, // or original_amount
                        date = apiTransaction.date,
                        icon = mapCategoryToIcon(apiTransaction.category),
                        category = apiTransaction.category
                    )
                }
            } else {
                emptyList()
            }
        } else {
            throw Exception("Search failed: ${response.errorBody()?.string()}")
        }
    }*/
    /*fun TransactionEntity.toTransactionRequest(): TransactionRequest {
        return TransactionRequest(
            title = this.title,
            original_amount = this.amount,
            original_currency = "USD", // You'll need to handle this properly
            date = this.date,
            category = this.category // Use the category string for Django
        )
    }
    suspend fun getFilteredTransactions(
        category: String? = null,
        ordering: String? = null
    ): List<TransactionEntity> {
        val response = apiService.getTransactions(category = category, ordering = ordering)

        if (response.isSuccessful) {
            val body = response.body()
            return if (body != null) {
                body.map { apiTransaction ->
                    TransactionEntity(
                        id = apiTransaction.id,
                        title = apiTransaction.title,
                        amount = apiTransaction.base_amount,
                        date = apiTransaction.date,
                        //icon = mapCategoryToIcon(apiTransaction.category),
                        category = apiTransaction.category
                    )
                }
            } else {
                emptyList()
            }
        } else {
            throw Exception("Filter failed: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getTransactionsWithAllFilters(
        category: String? = null,
        search: String? = null,
        ordering: String? = null
    ): List<TransactionEntity> {
        val response = apiService.getTransactions(
            category = category,
            search = search,
            ordering = ordering
        )

        if (response.isSuccessful) {
            val body = response.body()
            return if (body != null) {
                body.map { apiTransaction ->
                    TransactionEntity(
                        id = apiTransaction.id,
                        title = apiTransaction.title,
                        amount = apiTransaction.base_amount,
                        date = apiTransaction.date,
                        icon = mapCategoryToIcon(apiTransaction.category),
                        category = apiTransaction.category
                    )
                }
            } else {
                emptyList()
            }
        } else {
            throw Exception("Filter failed: ${response.errorBody()?.string()}")
        }
    }

    // Move the mapping function inside the class as a private method
    private fun mapCategoryToIcon(category: String): Int {
        return when (category.lowercase()) {
            "food" -> R.drawable.takeout
            "shopping" -> R.drawable.shopping
            "travel" -> R.drawable.travel
            "bills" -> R.drawable.bills
            "groceries" -> R.drawable.grocery
            "entertainment" -> R.drawable.entertaintment
            "transport" -> R.drawable.transport
            else -> 0 // default icon
        }
    }*/
}