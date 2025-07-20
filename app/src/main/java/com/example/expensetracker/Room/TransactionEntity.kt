package com.example.expensetracker.Room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)val id :Long = 0L,
    val title : String = "",
    val amount: Double = 0.0,
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
    val total: Float)

 data class MonthItem(
     val label: String,
     val value: String
 )
data class DailyData(
    var date: String="", // "yyyy-MM-dd"
    var total: Double=0.0,
    @Ignore
    var dayName: String="" // "Monday"
)
data class MonthlyData(
    var monthYear: String="", // Format: "YYYY-MM"
    var totalExpenses: Double=0.0,
    @Ignore
    var monthName: String="" ) // Make nullable with default)
data class WeeklyData(
    var weekStart: String = "",  // ← Now mutable with default
    var weekEnd: String = "",    // ← Now mutable with default
    var total: Double = 0.0,     // ← Now mutable with default
    @Ignore
    val label: String = ""       // ← Can stay immutable (ignored anyway)
)
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val amount: Double,
    val monthYear: String, // Format: "MM/yyyy" (e.g., "06/2023")
    val createdAt: Long = System.currentTimeMillis()
)
data class BudgetStatus(
    val budget: Double,
    val spent: Double
)