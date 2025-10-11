package com.example.expensetracker.ui.screens

import ExpenseBalanceSection
import MonthlyBudgetDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.models.AllTransac
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.BudgetCard
import com.example.expensetracker.ui.components.ExpenseItem
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.ui.components.CardItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController = rememberNavController(),
               viewModel: Transacviewmodel) {
    val context = LocalContext.current
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetAmount by remember { mutableStateOf("") }

    val budgetStatus by viewModel.budgetStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBudgetStatus()
    }
    LaunchedEffect(showBudgetDialog) {
        if (showBudgetDialog && budgetAmount.isEmpty()) {
            budgetAmount = if (viewModel.budgetStatus.value.budget > 0)
                viewModel.budgetStatus.value.budget.toInt().toString()
            else ""
        }
    }

    val (balanceFormatted, expenseFormatted) = remember(budgetStatus) {
        val balance = (budgetStatus.budget - budgetStatus.spent).coerceAtLeast(0.0)
        val expense = budgetStatus.spent
        Pair("₹${"%,.0f".format(balance)}", "₹${"%,.0f".format(expense)}")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color(0xFFA5BFC4)),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // HEADER SECTION - FIXED
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                // ❌ REMOVED: .height(IntrinsicSize.Min)
            ) {
                Column {
                    Text(
                        text = "BudgetWise",
                        fontSize = 30.sp,
                        color = Color(0xFF00332e),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Welcome Back naz",
                        fontSize = 18.sp,
                        color = Color(0xFF757575),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BUDGET CARD SECTION - FIXED
            Box(
                modifier = Modifier.fillMaxWidth()
                // ❌ REMOVED: .height(IntrinsicSize.Min)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BudgetCard(
                        isOverBudget = budgetStatus.spent > budgetStatus.budget,
                        budgetStatus = budgetStatus
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BALANCE/EXPENSE SECTION
            ExpenseBalanceSection(balance = balanceFormatted, expense = expenseFormatted)

            Spacer(modifier = Modifier.height(30.dp))

            // RECENT TRANSACTIONS SECTION
            Column {
                var isClicked by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See all",
                        fontSize = 16.sp,
                        color = if (isClicked) MaterialTheme.colorScheme.primary else Color.Black,
                        modifier = Modifier.clickable {
                            isClicked = !isClicked
                            navController.navigate(AllTransac)
                        }
                    )
                }

                val recentTransactions = viewModel.recentTransactions.collectAsState(initial = emptyList())
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(recentTransactions.value, key = { it.id }) { transaction ->
                        ExpenseItem(transaction = transaction, onClick = {})
                    }
                }
            }
        }

        MonthlyBudgetDialog(
            showDialog = showBudgetDialog,
            monthLabel = getCurrentMonthLabel(),
            budgetAmount = budgetAmount,
            onBudgetAmountChange = { budgetAmount = it },
            onDismiss = { showBudgetDialog = false },
            onSave = {
                budgetAmount.toDoubleOrNull()?.let { amount ->
                    viewModel.saveBudget(amount)
                }
                showBudgetDialog = false
            }
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentMonthLabel(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    return LocalDate.now().format(formatter)
}



@Composable
fun ArcBg(modifier: Modifier = Modifier, name: String) {
    Image(
        painter = painterResource(id = R.drawable.arc_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Keeps proportions correct
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp) // Increase height to ensure full visibility
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "good afternoon",
                //modifier = Modifier.padding (top = 20.dp),
                fontSize = 24.sp,  // Use sp instead of dp for text size
                color = Color.White,
                //fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = name,
                fontSize = 20.sp,  // Use sp instead of dp for text size
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Image(painter = painterResource(id = R.drawable.more_horiz), contentDescription = null)
    }
}                   /*Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .size(150.dp)
                            .align(Alignment.Center)
                            .offset(y = 130.dp)
                    ) {

                        CardItem(
                            modifier = Modifier,
                            onMenuClick = {
                                budgetAmount = if (budgetStatus.budget > 0.0) {
                                    budgetStatus.budget.toString()
                                } else {
                                    ""
                                }
                                showBudgetDialog = true
                            },
                            userName = "Naz",
                            balance = balanceFormatted,
                            expense = expenseFormatted,
                            isOverBudget = budgetStatus.spent > budgetStatus.budget,
                            budgetStatus = budgetStatus
                        )

                    }*/
@Composable
fun StatCard(
    title: String,
    amount: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp)
    ) {
        Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Text(amount, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}
/* @Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewHomeScreen() {
    // Create a test ViewModel instance
    val testViewModel = Transacviewmodel().apply {
        // Initialize with test data if needed
    }

    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            viewModel = testViewModel
        )
    }
}

 */