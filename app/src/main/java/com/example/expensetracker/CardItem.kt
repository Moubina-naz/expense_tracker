package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.Room.BudgetStatus

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    userName: String,
    balance: String,
    expense: String,
    isOverBudget: Boolean,
    budgetStatus: BudgetStatus // Add budget status parameter
) {
    val progress = if (budgetStatus.budget > 0.0) {
        (budgetStatus.spent / budgetStatus.budget).toFloat().coerceIn(0f, 1f)
    } else 0f
    val warningColor = if (isOverBudget) Color.Red else MaterialTheme.colorScheme.error
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(220.dp) // Increased height to accommodate budget
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.card))
    ) {
        // Header Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(horizontal = 16.dp)
            ) {
                cardrowitem(
                    modifier = Modifier.weight(1f),
                    title = "Balance",
                    amount = balance,
                    icon = R.drawable.arrow_downward_24
                )
                cardrowitem(
                    modifier = Modifier.weight(1f),
                    title = "Expense",
                    amount = expense,
                    icon = R.drawable.arrow_upward_24
                )

                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu_24),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

        }

        // Balance/Expense Row
        // Budget Progress Section
        budgetStatus?.let { status ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(16.dp)
            ) {
                // Budget vs Expense
                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                //val progress = (status.spent / status.budget).toFloat().coerceIn(0f, 1f)// Returns Double
                LinearProgressIndicator(
                    progress = { progress }, // Wrap in lambda for determinate progress
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (progress > 0.7f) Color.Red else Color.Green,
                    trackColor = Color.LightGray.copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Percentage Text
                Text(
                    text = "${(progress * 100).toInt()}% budget spent",
                    color = if (progress > 0.7f) Color.Red else Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
@Composable
fun cardrowitem(modifier: Modifier, title :String, amount:String, icon:Int) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp,fontWeight = FontWeight.Bold)

        }
        Text(
            text = amount,
            fontSize = 20.sp,  // Use sp instead of dp for text size
            color = Color.White
        )
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewCardItem(){
    CardItem(
        onMenuClick = {},
        userName = "Naz",
        balance = "₹10,000",
        expense = "₹6,000",
        isOverBudget = false,
        budgetStatus = BudgetStatus(budget = 10000.0, spent = 6000.0)
    )
}