package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.data.models.SearchTransac
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.data.models.UpdateTransac
import com.example.expensetracker.ui.components.ExpenseItem

@Composable
fun Alltransaction(
    modifier: Modifier = Modifier,
    viewModel: Transacviewmodel,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.arrowbackios),
                    contentDescription = "",
                    modifier = Modifier.clickable { navController.popBackStack() })


                Column {
                    Text(
                        text = "Expense History",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Box {
                    IconButton(onClick = {navController.navigate(SearchTransac)}) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "More Options",
                            tint = Color.Black
                        )
                    }



                }
            }


                    Spacer(modifier = Modifier.height(16.dp))
                    // Transactions List
                    val transactionList = viewModel.transactionList.collectAsState(initial = emptyList())
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                        items(transactionList.value, key = { it.id }) { transaction ->


                            val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.deleteTransaction(transaction)
                                    true
                                } else {
                                    false
                                }
                            })

                            // If swiped to delete, call delete
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
                                dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                            ) {
                                LaunchedEffect(transaction) {
                                    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deleteTransaction(transaction)
                                    }
                                }
                            }

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false, // Only allow swipe left to delete
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = colorResource(id = R.color.card)
                                        )
                                    }
                                }
                            ) {
                                ExpenseItem(
                                    transaction = transaction,
                                    onClick = {
                                        navController.navigate(UpdateTransac(transaction.id)) {
                                            viewModel.loadTransactionForEditing(transaction)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }



//@Composable
//@Preview(showBackground = true)
//fun AlltransactionPreview() {
//    Alltransaction(navController = NavController(LocalContext.current))
//}