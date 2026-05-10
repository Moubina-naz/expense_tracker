package com.example.expensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.data.models.BudgetStatus

import androidx.compose.runtime.remember
import com.example.expensetracker.utils.CurrencyManager
import com.example.expensetracker.data.models.UserPreferences

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
    val progress = when {
        budgetStatus.budget <= 0 -> 0f  // No budget set
        budgetStatus.spent >= budgetStatus.budget -> 1f  // Overspent (100%)
        else -> (budgetStatus.spent / budgetStatus.budget).toFloat()
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val currencySymbol = remember(userPrefs.getUserCurrency()) { 
        CurrencyManager.getCurrencySymbol(userPrefs.getUserCurrency())
    }

    val remaining = budgetStatus.budget - budgetStatus.spent
    val warningColor = if (isOverBudget) Color.Red else MaterialTheme.colorScheme.error
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(220.dp) // Increased height to accommodate budget
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.base))
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
                Spacer(modifier = Modifier.size(4.dp))
                cardrowitem(
                    modifier = Modifier.weight(1f),
                    title = "Expense",
                    amount = expense,
                    icon = R.drawable.arrow_upward_24
                )


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
                DotLinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = when {
                        progress >= 1f -> Color.Red
                        progress > 0.7f -> Color.Yellow
                        else -> Color.Green
                    },
                    trackColor = Color.LightGray.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Percentage Text
                Text(
                    text = "${(progress * 100).toInt()}% budget spent",
                    color = if (progress > 0.7f) Color.Red else Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
@Composable
fun BudgetCard(
    isOverBudget: Boolean,
    budgetStatus: BudgetStatus,
    modifier: Modifier = Modifier
) {
    // ---- Logic ----
    val progress = when {
        budgetStatus.budget <= 0 -> 0f  // No budget set
        budgetStatus.spent >= budgetStatus.budget -> 1f  // Overspent (100%)
        else -> (budgetStatus.spent / budgetStatus.budget).toFloat()
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val currencySymbol = remember(userPrefs.getUserCurrency()) { 
        CurrencyManager.getCurrencySymbol(userPrefs.getUserCurrency())
    }

    val remaining = budgetStatus.budget - budgetStatus.spent
    val balanceFormatted = "${currencySymbol}${"%,.0f".format(remaining.coerceAtLeast(0.0))}"


    Card(
        modifier = modifier
            .width(350.dp)   // Fixed width for compact look
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC5F1E8)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth() // Changed from IntrinsicSize.Min to fillMaxWidth
        ) {
            budgetStatus?.let { status ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // This spaces items properly
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Budget for this month",
                            color = Color.Gray.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium
                        )

                        // Percentage badge - no align modifier needed
                        Text(
                            text = "${(progress * 100).toInt()}% spent",
                            color = when {
                                progress >= 1f -> Color.Red
                                progress > 0.7f -> Color(0xFFFF6D00)
                                else -> Color.Gray
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = when {
                            progress >= 1f -> Color.Red
                            progress > 0.7f -> Color(0xFFFFAA00) // Orange
                            else -> Color(0xFF2DB7B2) // Green
                        },
                        trackColor = Color.LightGray.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun cardrowitem(modifier: Modifier, title: String, amount: String, icon: Int) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title, 
                color = Color.White.copy(alpha = 0.8f), 
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount,
            style = MaterialTheme.typography.displaySmall,
            color = Color.White
        )
    }
}
@Composable
fun DotLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    trackColor: Color = Color.LightGray.copy(alpha = 0.4f),
    lineHeight: Dp = 3.dp,   // thinner line
    dotRadius: Dp = 6.dp     // keep dot big enough
) {
    Canvas(modifier = modifier.height(dotRadius * 2)) {
        val canvasWidth = size.width
        val centerY = size.height / 2
        val progressWidth = (progress.coerceIn(0f, 1f)) * canvasWidth

        // Draw background track (thin line)
        drawRoundRect(
            color = trackColor,
            cornerRadius = CornerRadius(x = lineHeight.toPx() / 2, y = lineHeight.toPx() / 2),
            topLeft = Offset(0f, centerY - lineHeight.toPx() / 2),
            size = Size(width = canvasWidth, height = lineHeight.toPx())
        )

        // Draw progress line (thin)
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(x = lineHeight.toPx() / 2, y = lineHeight.toPx() / 2),
            topLeft = Offset(0f, centerY - lineHeight.toPx() / 2),
            size = Size(width = progressWidth, height = lineHeight.toPx())
        )

        // Draw dot at the end of progress
        if (progress > 0f) {
            drawCircle(
                color = color,
                radius = dotRadius.toPx(),
                center = Offset(progressWidth, centerY)
            )
        }
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