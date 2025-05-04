package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen( navController: NavController = rememberNavController(),
                viewModel : Transacviewmodel) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
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
                        IconButton(onClick = { /* Navigate to Home */ }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }
                        IconButton(onClick = { /* Navigate to Wallet */ }) {
                            Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Wallet")
                        }
                    }


                    Box(
                        modifier = Modifier
                            .size(60.dp).clip(RoundedCornerShape(16.dp)) // square size
                            .background(color = colorResource(id = R.color.base)) // square background
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
                        IconButton(onClick = { /* Navigate to Statistics */ }) {
                            Icon(Icons.Filled.BarChart, contentDescription = "Statistics")
                        }
                        IconButton(onClick = { /* Navigate to Settings */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        }
                    }
                }
            }
        }
    )
    { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    ArcBg(name = "John Doe")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .size(150.dp)
                        .align(Alignment.Center)
                        .offset(y = 150.dp)
                ) {
                    CardItem(
                        modifier = Modifier
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            Column {
                var isClicked by remember { mutableStateOf(false) }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    Text(text = "Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "See all",
                        fontSize = 16.sp,
                        color = if (isClicked) MaterialTheme.colorScheme.primary else Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { isClicked = !isClicked
                                navController.navigate(AllTransac) })
                }
                val recentTransactions = viewModel.recentTransactions.collectAsState(initial = emptyList())
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(
                        items = recentTransactions.value,
                        key = { it.id }
                    ) { transaction ->
                        TransactionItem(
                            transaction = transaction, // Should be singular
                            onClick = {}
                        )
                    }
                }
        }


    }
}
}



@Composable
fun ArcBg(modifier: Modifier = Modifier, name: String) {
    Image(
        painter = painterResource(id = R.drawable.arc_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Keeps proportions correct
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp) // Increase height to ensure full visibility
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "good afternoon",
                //modifier = Modifier.padding (top = 20.dp),
                fontSize = 24.sp,  // Use sp instead of dp for text size
                color = Color.White,
                //fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = name,
                fontSize = 20.sp,  // Use sp instead of dp for text size
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Image(painter = painterResource(id = R.drawable.more_horiz), contentDescription = null)
    }
}

/*@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewHomeScreen() {
    // Create a test ViewModel instance
    val testViewModel = Transacviewmodel().apply {
        // Initialize with test data if needed
    }

    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            viewModel = testViewModel
        )
    }
}

 */