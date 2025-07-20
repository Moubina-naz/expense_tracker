package com.example.expensetracker

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen( navController: NavController = rememberNavController(),
                viewModel : Transacviewmodel) {
    val context = LocalContext.current
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetAmount by remember { mutableStateOf("") }

    val budgetStatus by viewModel.budgetStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBudgetStatus()
    }
    LaunchedEffect(showBudgetDialog) {
        if (showBudgetDialog) {
            // Only update if we don't already have a value being edited
            if (budgetAmount.isEmpty()) {
                budgetAmount = if (viewModel.budgetStatus.value.budget > 0)
                    viewModel.budgetStatus.value.budget.toInt().toString()
                else ""
            }
        }
    }

    val (balanceFormatted, expenseFormatted) = remember(budgetStatus) {
        val balance = (budgetStatus.budget - budgetStatus.spent).coerceAtLeast(0.0)
        val expense = budgetStatus.spent

        Pair(
            "₹${"%,.0f".format(balance)}",
            "₹${"%,.0f".format(expense)}"
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

    )
    { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    ArcBg(name = "John Doe")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .size(150.dp)
                        .align(Alignment.Center)
                        .offset(y = 150.dp)
                ) {

                    CardItem(
                        modifier = Modifier,
                        onMenuClick = {    budgetAmount = if (budgetStatus.budget > 0.0) {
                            budgetStatus.budget.toString()
                        } else {
                            ""
                        }
                            showBudgetDialog = true},
                        userName = "Naz",
                        balance = balanceFormatted,
                        expense = expenseFormatted,
                        isOverBudget = budgetStatus.spent > budgetStatus.budget,
                        budgetStatus = budgetStatus
                    )

                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                var isClicked by remember { mutableStateOf(false) }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    Text(text = "Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "See all",
                        fontSize = 16.sp,
                        color = if (isClicked) MaterialTheme.colorScheme.primary else Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { isClicked = !isClicked
                                navController.navigate(AllTransac) })
                }
                val recentTransactions = viewModel.recentTransactions.collectAsState(initial = emptyList())
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(
                        items = recentTransactions.value,
                        key = { it.id }
                    ) { transaction ->
                        TransactionItem(
                            transaction = transaction, // Should be singular
                            onClick = {}
                        )
                    }
                }
        }


    }
        MonthlyBudgetDialog(
            showDialog = showBudgetDialog,
            monthLabel = getCurrentMonthLabel(),
            budgetAmount = budgetAmount,
            onBudgetAmountChange = { budgetAmount=it},
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
}

/*@Composable
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