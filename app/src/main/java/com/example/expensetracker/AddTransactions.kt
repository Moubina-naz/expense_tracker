package com.example.expensetracker

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Transaction
import com.example.expensetracker.Room.TransactionEntity

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
                Dataform(
                    id = 0L,
                    viewmodel = viewModel,
                    navController = navController
                )
            }
        }
    }
}




/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true,showSystemUi = true)
@Composable
fun PreviewTransacTextField() {
    val mockViewModel = Transacviewmodel().apply {
        transacTitlestate = "Mock title" // Pretend user input
        transacAmountstate = "100" // Pretend user input
        transacDatestate = "2023-09-20" // Pretend user input
        selectedCategory = categories.first() // Pretend user selection
    }

    dataform(id = 1L, viewmodel = mockViewModel, navController = rememberNavController())

}

 */





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dataform(
    modifier: Modifier = Modifier,
    id: Long,
    viewmodel: Transacviewmodel, // Non-nullable
    navController: NavController,
    transaction: Transaction? = null  // null means new transaction

    ) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(Color.White)
            .padding(20.dp)) {


        Spacer(modifier = Modifier.height(10.dp))

        transacTextfeild(
            lable = "Title",
            value = viewmodel?.transacTitlestate ?: "",
            onValueChange = { viewmodel?.onTransacTitleChange(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        transacTextfeild(
            lable = "Amount",
            value = viewmodel?.transacAmountstate ?: "",
            onValueChange = { viewmodel?.onTransacAmountChange(it) },
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(20.dp))

        PickDate(label = "Date",
            value = viewmodel?.transacDatestate ?: "",
            onDateSelected = { date ->
                viewmodel?.onTransacDateChange(date)
            },
            modifier = Modifier.fillMaxWidth())


        Spacer(modifier = Modifier.height(20.dp))

        val t = TransactionEntity(
            id = viewmodel.currentEditingId ?: 0L,
            title = viewmodel.transacTitlestate,
            amount = viewmodel.transacAmountstate,
            date = viewmodel.transacDatestate,
            icon = viewmodel.transacIconstate
        )
        val Categories = viewmodel?.categories ?: emptyList()

        Box(modifier = Modifier.height(300.dp)) {
            CategoryDropdownGrid(
                cats = viewmodel?.categories ?: emptyList(), // ✅ use VM data
                selectedCategory = viewmodel?.selectedCategory,
                onCategorySelected = {category-> viewmodel?.onCategorySelected(category) }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        val context = LocalContext.current
        Button(
            onClick = {
                if (viewmodel.transacTitlestate.isNotEmpty() &&
                    viewmodel.transacAmountstate.isNotEmpty() &&
                    viewmodel.transacDatestate.isNotEmpty()) {

                    viewmodel.addTransaction(t)
                    Toast.makeText(
                        context,
                        if (id != 0L) "Transaction Updated" else "Transaction Added",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(AllTransac) {
                        popUpTo(HomeScrn) { inclusive = false } // Always go back to Home
                    }}

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(if (transaction != null) "Update" else "Save")
        }

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
                style = TextStyle(color = Color.Black),
                fontSize = 16.sp  // Explicitly set fontSize to resolve ambiguity
            )
        },
        modifier = Modifier.fillMaxWidth(),

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            cursorColor = Color.Black
        ))

}
@Composable
fun Addbg( title:  String,onBackClick: () -> Unit){

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
                //modifier = Modifier.padding (top = 20.dp),
                fontSize = 24.sp,  // Use sp instead of dp for text size
                color = Color.White,
                //fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))

        }
        Image(painter = painterResource(id = R.drawable.more_horiz),contentDescription = null )
    }
}