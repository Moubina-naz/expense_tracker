package com.example.expensetracker.ui.screens

import StatisticsScreen
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.ui.components.LineChartScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: Transacviewmodel,
    navController: NavController
) {


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
        val options = listOf("Breakdown", "Trend")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Fixed height
                .padding(horizontal = 16.dp)
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            )
            {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = colorResource(id = R.color.base),
                            inactiveContainerColor = Color.White,
                            activeContentColor = Color.White,
                            inactiveContentColor = colorResource(id = R.color.base)

                        )
                    ) {
                        Text(label)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        val transactions by viewModel.transactionList.collectAsState(initial = emptyList())

        when (selectedIndex) {
            0 -> StatisticsScreen(viewModel = viewModel, navController)
            1 -> LineChartScreen(viewModel)

            /*              LineChartScreen(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(300.dp),
                  timescale = TimeScale.WEEKLY,
                  points = transactions
                      .groupBy { LocalDate.parse(it.date) } // Group by parsed LocalDate directly
                      .map { (date, transactionGroup) ->
                          DataPoint(
                              transactionGroup.sumOf { it.amount.toDouble() }.toFloat(),
                              date
                          )
                      }
                      .sortedBy { it.date },
                  //onPointClicked = { point -> }
              )
              }
 */
        }


    }
}




/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = NavController(LocalContext.current))
}

 */