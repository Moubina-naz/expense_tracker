package com.example.expensetracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.data.models.TransactionEntity


import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.utils.CurrencyManager
import com.example.expensetracker.data.models.UserPreferences

@Composable
fun ExpenseItem(transaction: TransactionEntity, onClick: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val currencySymbol = remember(userPrefs.getUserCurrency()) { 
        CurrencyManager.getCurrencySymbol(userPrefs.getUserCurrency())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp).clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.itemColor))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                val safeIcon = if (transaction.icon != 0) transaction.icon else R.drawable.shopping

                Image(
                    painter = painterResource(id = safeIcon),
                    contentDescription = "",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))

                Column {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = transaction.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }

            val formattedAmount = remember(transaction.amount) {
                "%,.2f".format(transaction.amount)
            }
            Text(
                text = "${currencySymbol}${formattedAmount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun previewRecentTransactions() {

}

