package com.example.expensetracker.ui.screens

import MonthlyBudgetDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
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
import com.example.expensetracker.viewmodels.Transacviewmodel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(navController: NavController = rememberNavController(),
                   viewModel : Transacviewmodel
) {
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetAmount by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 16.dp, end = 16.dp)
        ) {
            // Back Arrow on the left
            Icon(
                painter = painterResource(id = R.drawable.arrowbackios),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { /* navController.popBackStack() */ }
            )

            // Title in the center
            Text(
                text = "Settings",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Items
        MenuItem(title = "Profile", icon = Icons.Default.Person, onClick = { navController.navigate(
            EditProfileScrn
        ) })

    MenuItem(title = "Currency", value = "INR", icon = Icons.Default.AttachMoney, onClick = { navController.navigate(CurrencyScrn)})
    MenuItem(title = "Budget", value = "Off", icon = Icons.Default.NotificationsOff, onClick = { showBudgetDialog = true })
    MenuItem(title = "Account", icon = Icons.Default.ThumbUp, onClick = { })


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
@Composable
fun MenuItem(title: String, value: String? = null, icon: ImageVector ,onClick: () -> Unit,) {
    Row(
        modifier = Modifier.clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color.Black)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp)
        if (value != null) {
            Text(value, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    //ProfileScreen()
}