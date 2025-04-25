package com.example.expensetracker

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun HomeScreen( navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        /*topBar = {
            Topbarview(title = "Expense Tracker") {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            }
        },

         */
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center // Ensures FAB is centered
            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                ) {

                    FloatingActionButton( // wht about this bitch also little tip instead of raw dagging and searching press shift two times and search for anything in the project
                        modifier = Modifier.padding(all = 20.dp),
                        contentColor = Color.White,
                        containerColor = colorResource(id = R.color.base),

                        onClick = {  navController.navigate(AddTransac) {
                            popUpTo(AddTransac) { inclusive = true } }

                        }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
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
            RecentTransactions()
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

@Composable
@Preview(showBackground = true)
fun previewHomeScreen() {
    HomeScreen()
}
