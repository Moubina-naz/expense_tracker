package com.example.expensetracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateTransactions(
        modifier: Modifier = Modifier,
        id: Long ,
        viewModel: Transacviewmodel = viewModel(), // Single instance
        navController: NavController  // Single instance

    ) {
    LaunchedEffect(id) {
        viewModel.getTransacById(id)?.let { transaction ->
            viewModel.loadTransactionForEditing(transaction)
        }
    }

        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.TopCenter)
            ) {
                Addbg(
                    "Update Transaction",
                    onBackClick = { navController.popBackStack()})
            }

            // Form Content (PUSHED UP using padding from top instead of align bottom)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp) // 👈 Try 120.dp or less to move form up
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // 👈 makes the form fill remaining space
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    shadowElevation = 2.dp,
                    color = Color.White
                ) {
                    /*val t = viewModel.getTransacById(id)

                    LaunchedEffect(t)  {
                        if (t != null) {
                            viewModel.onTransacTitleChange(t.title)
                            viewModel.onTransacAmountChange(t.amount)
                            viewModel.onTransacIconChange(t.icon)
                            viewModel.onTransacDateChange(t.date)
                            //iewModel.onTransacIdChange(t.id)
                        }
                    }



                     */
                    Dataform(
                        id = id,
                        viewmodel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }


