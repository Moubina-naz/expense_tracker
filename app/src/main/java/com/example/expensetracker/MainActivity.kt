package com.example.expensetracker

import BudgetWiseHomeScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.models.Navigation
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

import com.example.expensetracker.viewmodels.Transacviewmodel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: Transacviewmodel by viewModels()

        // Call debug logging
        viewModel.debugPrintTransactions()
        setContent {
            ExpenseTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background) {
            val  viewModel: Transacviewmodel = viewModel()
            val navController = rememberNavController()

           Navigation(viewModel,navController)

            //BudgetWiseHomeScreen()
            }
           
        }
            }
        }
    }


