package com.example.expensetracker.ui.screens

import MonthlyBudgetDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.data.models.CurrencyScrn
import com.example.expensetracker.data.models.EditProfileScrn
import com.example.expensetracker.ui.components.BudgetWiseTopBar
import com.example.expensetracker.ui.theme.MutedGray
import com.example.expensetracker.ui.theme.SoftDarkGray
import com.example.expensetracker.viewmodels.Transacviewmodel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(navController: NavController = rememberNavController(),
                   viewModel : Transacviewmodel
) {
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showKeyDialog by remember { mutableStateOf(false) }
    var budgetAmount by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf(viewModel.getApiKey() ?: "") }
    val budgetStatus by viewModel.budgetStatus.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { com.example.expensetracker.data.models.UserPreferences(context) }
    val currentCurrency = userPrefs.getUserCurrency()
    val currencySymbol = remember(currentCurrency) {
        com.example.expensetracker.utils.CurrencyManager.getCurrencySymbol(currentCurrency)
    }

    LaunchedEffect(Unit) {
        viewModel.loadBudgetStatus()
    }

    LaunchedEffect(showBudgetDialog) {
        if (showBudgetDialog) {
            budgetAmount = if (budgetStatus.budget > 0)
                budgetStatus.budget.toInt().toString()
            else ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BudgetWiseTopBar(
            title = "Settings",
            showBack = true,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            MenuItem(title = "Profile", icon = Icons.Default.Person, onClick = {
                navController.navigate(EditProfileScrn)
            })

            MenuItem(
                title = "Currency",
                value = currentCurrency,
                icon = Icons.Default.AttachMoney,
                onClick = { navController.navigate(CurrencyScrn) })

            val budgetValue = if (budgetStatus.budget > 0) "$currencySymbol${budgetStatus.budget.toInt()}" else "Not Set"
            MenuItem(
                title = "Budget",
                value = budgetValue,
                icon = if (budgetStatus.budget > 0) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                onClick = { showBudgetDialog = true }
            )

            MenuItem(
                title = "AI Insights Key",
                value = if (userPrefs.getGeminiApiKey().isNullOrBlank()) "Not Set" else "••••••••",
                icon = Icons.Default.Key,
                onClick = {
                    showKeyDialog = true
                })
            
            MenuItem(title = "Support", icon = Icons.Default.ThumbUp, onClick = { })
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

        if (showKeyDialog) {
            AlertDialog(
                onDismissRequest = { showKeyDialog = false },
                title = { Text("Gemini API Key", style = MaterialTheme.typography.titleLarge) },
                text = {
                    Column {
                        Text(
                            "Enter your Google AI API key to enable spending insights.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { apiKey = it },
                            label = { Text("API Key") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.saveGeminiApiKey(apiKey)
                        showKeyDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showKeyDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
@Composable
fun MenuItem(title: String, value: String? = null, icon: ImageVector ,onClick: () -> Unit,) {
    Row(
        modifier = Modifier.clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = SoftDarkGray, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, 
            modifier = Modifier.weight(1f), 
            style = MaterialTheme.typography.titleLarge
        )
        if (value != null) {
            Text(
                text = value, 
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGray
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight, 
            contentDescription = "Arrow", 
            tint = MutedGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    //ProfileScreen()
}