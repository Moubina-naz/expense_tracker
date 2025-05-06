package com.example.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Alltransaction(
    modifier: Modifier = Modifier,
    viewModel: Transacviewmodel,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {

            val expanded = remember { mutableStateOf(false) }
            Addbg("Previous Transactions", onBackClick = { navController.popBackStack() },
                expanded = expanded,
                onMoreClick = {
                    DropdownMenuItem(text = { Text(text = "Search",color = Color.Black) },
                        onClick = { expanded.value=false
                        navController.navigate(SearchTransac)})
                    DropdownMenuItem(text = { Text(text = "Sort",color = Color.Black) }, onClick = {expanded.value=false  })
                    DropdownMenuItem(text = { Text(text = "Filter",color = Color.Black) }, onClick = { expanded.value=false })
                })
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                shadowElevation = 2.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transactions",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Date",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { }
                        )
                    }

                    // Transactions List
                    val transactionList = viewModel.transactionList.collectAsState(initial = emptyList())
                    LazyColumn {
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
                                            .background(Color.White)
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
                                TransactionItem(
                                    transaction = transaction,
                                    onClick = {
                                        viewModel.loadTransactionForEditing(transaction)
                                        navController.navigate(UpdateTransac(transaction.id))
                                    }
                                )
                            }
                        }
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