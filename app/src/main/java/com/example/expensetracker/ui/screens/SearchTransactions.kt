package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.data.models.UpdateTransac
import com.example.expensetracker.ui.components.ExpenseItem

@Composable
fun SearchTransactions(
    modifier: Modifier = Modifier,
    viewModel: Transacviewmodel,
    navController: NavController,
) {
    // Reset search when entering this screen
    LaunchedEffect(Unit) {
        viewModel.updateSearchQuery("") // Clear any previous search
    }

    SearchScreen(
        modifier = modifier.fillMaxSize(),
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: Transacviewmodel,
    navController: NavController
) {
    var localSearchQuery by remember { mutableStateOf("") }
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState() // CHANGED THIS LINE

    // Sync with ViewModel
    LaunchedEffect(localSearchQuery) {
        viewModel.updateSearchQuery(localSearchQuery)
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = localSearchQuery,
            onValueChange = { localSearchQuery = it },
            placeholder = { Text("Search by title, category, amount...") },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (localSearchQuery.isNotBlank()) {
                    IconButton(onClick = { localSearchQuery = "" }) {
                        Icon(Icons.Outlined.Cancel, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search results - SIMPLIFIED
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (localSearchQuery.isNotBlank() && searchResults.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No transactions found for '$localSearchQuery'")
            }
        } else {
            if (localSearchQuery.isNotBlank()) {
                Text(
                    text = "Found ${searchResults.size} results for \"$localSearchQuery\"",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyColumn {
                items(searchResults) { transaction ->
                    ExpenseItem(
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