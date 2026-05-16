package com.example.expensetracker.ui.screens

import CategoryDropdownGrid
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Transaction
import com.example.expensetracker.data.models.AllTransac
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.ui.components.PickDate
import com.example.expensetracker.R
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.ui.components.BudgetWiseTopBar
import com.example.expensetracker.ui.theme.MutedGray
import com.example.expensetracker.ui.theme.SoftDarkGray
import com.example.expensetracker.viewmodels.Transacviewmodel

@RequiresApi(Build.VERSION_CODES.O)
@Composable


fun AddTransactions(
    modifier: Modifier = Modifier,
    viewModel: Transacviewmodel = viewModel(), // Single instance
    navController: NavController = rememberNavController() // Single instance

) {

    LaunchedEffect(Unit) {
        viewModel.clearFields()
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
                "Add Transaction",
                onBackClick = { navController.popBackStack()})
        }
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
                Dataform(
                    id = 0L,
                    viewmodel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dataform(
    modifier: Modifier = Modifier,
    id: Long,
    viewmodel: Transacviewmodel,
    navController: NavController,
    transaction: Transaction? = null ,
    isUpdate: Boolean = false

    ) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val userPreferences = remember { com.example.expensetracker.data.models.UserPreferences(context) }
    val currencySymbol = remember(userPreferences.getUserCurrency()) { 
        com.example.expensetracker.utils.CurrencyManager.getCurrencySymbol(userPreferences.getUserCurrency()) 
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState) // Make sure this is the main scroll container
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        transacTextfeild(
            lable = "Title",
            value = viewmodel.transacTitlestate,
            onValueChange = { viewmodel.onTransacTitleChange(it) }
        )

        Spacer(modifier = Modifier.height(28.dp))

        transacTextfeild(
            lable = "Amount ($currencySymbol)",
            value = viewmodel.transacAmountstate,
            onValueChange = { viewmodel.onTransacAmountChange(it) },
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(28.dp))

        PickDate(
            label = "Date",
            value = viewmodel.transacDatestate,
            onDateSelected = { date -> viewmodel.onTransacDateChange(date) },
            viewModel = viewmodel,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Remove the fixed height constraint and let it take needed space
        CategoryDropdownGrid(
            cats = viewmodel.categories,
            selectedCategory = viewmodel.selectedCategory,
            onCategorySelected = { category -> viewmodel.onCategorySelected(category) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp)) // Add more space before button

        val t = TransactionEntity(
            id = viewmodel.currentEditingId ?: 0L,
            title = viewmodel.transacTitlestate,
            amount = viewmodel.transacAmountstate.toDoubleOrNull() ?: 0.0,
            date = viewmodel.transacDatestate,
            icon = viewmodel.transacIconstate,
            category = viewmodel.selectedCategory?.name ?: ""
        )

        Button(
            onClick = {
                if (viewmodel.transacTitlestate.isNotEmpty() &&
                    viewmodel.transacAmountstate.isNotEmpty() &&
                    viewmodel.transacDatestate.isNotEmpty()) {

                    if (id != 0L) {
                        // UPDATE EXISTING TRANSACTION
                        viewmodel.updateTransaction(t) // ← Call update instead of add
                        Toast.makeText(context, "Transaction Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        // ADD NEW TRANSACTION
                        viewmodel.addTransaction(t)
                        Toast.makeText(context, "Transaction Added", Toast.LENGTH_SHORT).show()
                    }

                    navController.navigate(AllTransac) {
                        popUpTo(HomeScrn) { inclusive = false }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.base),
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (id != 0L) "Update" else "Save",
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Add extra space at the bottom to ensure button is visible
        Spacer(modifier = Modifier.height(50.dp))
    }

    // Scroll to bottom when keyboard appears (optional)
    LaunchedEffect(Unit) {
        scrollState.scrollTo(scrollState.maxValue)
    } 
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun transacTextfeild(
    lable: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
            modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = lable,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGray
            )
        },
        modifier = Modifier.fillMaxWidth(),

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedTextColor = SoftDarkGray,
            unfocusedTextColor = SoftDarkGray,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = colorResource(id = R.color.base), // Use base color from resources or a defined constant
            unfocusedIndicatorColor = MutedGray.copy(alpha = 0.3f),
            focusedLabelColor = colorResource(id = R.color.base),
            unfocusedLabelColor = MutedGray,
            cursorColor = colorResource(id = R.color.base)
        ))

}
@Composable
fun Addbg( title:  String,onBackClick: () -> Unit,expanded: MutableState<Boolean> = remember { mutableStateOf(false) },
           showMoreButton: Boolean = true,
           onMoreClick: @Composable ColumnScope.()-> Unit = {}){

    Image(
        painter = painterResource(id = R.drawable.arc_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Keeps proportions correct
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Increase height to ensure full visibility
    )
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(top = 60.dp, start = 16.dp, end = 16.dp),horizontalArrangement = Arrangement.SpaceBetween){

        Image(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "back", modifier = Modifier.clickable { onBackClick() })


        Column{
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(8.dp))

        }
        if (showMoreButton) {  // Only show the more button if enabled
            Box {
                IconButton(onClick = { expanded.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_horiz),
                        contentDescription = "More Options",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    onMoreClick()
                }
            }
        } else {
            // Add an empty box to maintain the Row's spacing
            Box(modifier = Modifier.size(48.dp))  // Match the IconButton size
        }
    }
}
