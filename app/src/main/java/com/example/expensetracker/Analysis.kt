package com.example.expensetracker

import ChartCirclePie
import StatisticsScreen
import android.os.Build
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
//viewModel: Transacviewmodel
    navController: NavController
) {


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(painter = painterResource(id = R.drawable.arrowbackios), contentDescription = "",  modifier = Modifier.clickable { navController.popBackStack() })


            Column() {
                Text(
                    text = "Expense Statistics",
                    //modifier = Modifier.padding (top = 20.dp),
                    fontSize = 24.sp,  // Use sp instead of dp for text size
                    color = Color.Black,
                    //fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(8.dp))

            }
            Box {
                IconButton(onClick = { true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_horiz),
                        contentDescription = "More Options",
                        tint = Color.Black
                    )
                }

                // Anchor menu to the box
                DropdownMenu(
                    expanded = false,
                    onDismissRequest = { false },
                    modifier = Modifier.background(Color.White) // Fix invisible menu
                ) {

                }

            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        var selectedIndex by remember { mutableStateOf(0) }
        val options = listOf("Breakdown","Trend" )
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Color(669999)  ,
                        inactiveContainerColor = Color.White,
                        activeContentColor = Color.White,
                        inactiveContentColor = Color(669999)

                    )
                ) {
                    Text(label)
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            when(selectedIndex){
                0->StatisticsScreen()
                //1->LineChartScreen()
            }


        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = NavController(LocalContext.current))
}