package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.data.models.SplashScrn
import com.example.expensetracker.data.models.StartupScrn
import com.example.expensetracker.data.models.UserPreferences

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFB6EEE3) // Light neutral background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular Icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF44B4B0)), // Wallet green
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet Icon",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BudgetWise",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFF212121)
            )

            Text(
                text = "Your Personal Expense Tracker",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF757575)
            )
        }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000) // 2 sec splash
            if (userPreferences.isUserInfoCompleted()) {
                navController.navigate(HomeScrn) {
                    popUpTo(SplashScrn) { inclusive = true }
                }
            } else {
                navController.navigate(StartupScrn) {
                    popUpTo(SplashScrn) { inclusive = true }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("USD") } // Default value
    var expanded by remember { mutableStateOf(false) }
    val currencies = com.example.expensetracker.utils.CurrencyManager.supportedCurrencies

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to BudgetWise!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please enter your details to get started.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = "$currency - ${com.example.expensetracker.utils.CurrencyManager.getCurrencyName(currency)}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencies.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text("$selectionOption - ${com.example.expensetracker.utils.CurrencyManager.getCurrencyName(selectionOption)}") },
                            onClick = {
                                currency = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && age.isNotBlank() && country.isNotBlank() && currency.isNotBlank()) {
                        userPreferences.saveUserInfo(name, age, country, currency)
                        navController.navigate(HomeScrn) {
                            popUpTo(StartupScrn) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = name.isNotBlank() && age.isNotBlank() && country.isNotBlank() && currency.isNotBlank()
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}