package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.Room.TransactionEntity



@Composable
fun TransactionItem(transaction: TransactionEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onClick() },

        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),

                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.base))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Pushes date to right
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = transaction.icon),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Column {
                    Text(text = transaction.title, fontSize = 18.sp)
                    Text(text = transaction.amount.toString(), fontSize = 20.sp)
                }
            }

            Text(
                text = transaction.date,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterVertically) // Ensures vertical alignment
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun previewRecentTransactions() {
}

