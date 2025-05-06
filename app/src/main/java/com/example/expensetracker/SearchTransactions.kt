package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun SearchTransactions(modifier: Modifier = Modifier,viewModel : Transacviewmodel,navController: NavController) {
    Box(modifier=modifier.fillMaxSize()){
        Search(modifier= modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),viewModel,navController )
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  Search(modifier: Modifier=Modifier,viewModel : Transacviewmodel,navController: NavController ) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.filteredTransactions.collectAsState()
    val alltransactions by viewModel.transactionList.collectAsState()
     var isActive by remember { mutableStateOf(false) }
    
    SearchBar(query = searchQuery,
        onQueryChange = {new->
                        viewModel.updateSearchQuery(new)
        },
        onSearch = {
                   isActive = false
        },
        active = isActive ,
        onActiveChange = {
            isActive = it
        },
        placeholder = {
            Text(text = "search Transactions...")
        },
        trailingIcon = {
            if(isActive){
                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "")

                }
            }
            else{
                IconButton(onClick = { isActive = false }) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "")

                }
            }
        })
    {
        when {
            isSearching -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            searchResults.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Transactions Found")
                }
            }

            else -> {
                LazyColumn {
                    items(searchResults) { transaction->
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
    if (!isActive && searchQuery.isEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(alltransactions) { transaction ->
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

