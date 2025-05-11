package com.example.expensetracker.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)val id :Long = 0L,
    val title : String = "",
    val amount : String ="",
    val date : String = "",
    val icon : Int = 0,
    val category : String = ""
)
@Serializable
data class CategoryItem(
    val name: String,
    val iconRes: Int
)

data class CategoryTotal(
    val category: String,
    val total: Double )

data class MonthlySummary(
    val monthYear: String, // Format: "MM/YYYY"
    val totalExpenses: Double,

)
data class MonthlyData(
    val monthYear: String, // Format: "MM/YYYY"
    val totalExpenses: Double,
    val year: String,
    val monthName : String,
    val categoryBreakdown: List<CategoryTotal>
)


