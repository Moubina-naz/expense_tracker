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
    val icon : Int = 0
)
@Serializable
data class CategoryItem(
    val name: String,
    val iconRes: Int
)




