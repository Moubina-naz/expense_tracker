package com.example.expensetracker.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.expensetracker.data.models.AddTransac
import com.example.expensetracker.data.models.AllTransac
import com.example.expensetracker.data.models.DashboardTransac
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.R

import com.example.expensetracker.data.models.SearchTransac
import com.example.expensetracker.data.models.SettingsScrn
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.data.models.UpdateTransac
import com.example.expensetracker.ui.screens.SearchTransactions
import com.example.expensetracker.ui.screens.UpdateTransactions
import com.example.expensetracker.ui.screens.AddTransactions
import com.example.expensetracker.ui.screens.Alltransaction
import com.example.expensetracker.ui.screens.DashboardScreen
import com.example.expensetracker.ui.screens.HomeScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side icons
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = { navController.navigate(HomeScrn) }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }
                IconButton(onClick = { navController.navigate(AllTransac) }) {
                    Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Wallet")
                }
            }

            // FAB
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = colorResource(id = R.color.base))
                    .clickable { navController.navigate(AddTransac) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Right side icons
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = { navController.navigate(DashboardTransac) }) {
                    Icon(Icons.Filled.BarChart, contentDescription = "Statistics")
                }
                IconButton(onClick = { navController.navigate(SettingsScrn)}) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        }
    }
}
