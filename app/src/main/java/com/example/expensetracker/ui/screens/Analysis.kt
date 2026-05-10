package com.example.expensetracker.ui.screens

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.ui.components.LineChartScreen
import com.example.expensetracker.ui.components.StatisticsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: Transacviewmodel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA)) // Light grayish background from image
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrowbackios),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(24.dp).clickable { navController.popBackStack() }
            )

            Text(
                text = "Expense Statistics",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Icon(
                painter = painterResource(id = R.drawable.more_horiz),
                contentDescription = "More Options",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.size(32.dp))

        var selectedIndex by remember { mutableStateOf(0) }
        val options = listOf("Breakdown", "Trend")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(),
                space = 0.dp
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFF649594), // Teal color from image
                            inactiveContainerColor = Color.White,
                            activeContentColor = Color.White,
                            inactiveContentColor = Color(0xFF649594),
                            activeBorderColor = Color(0xFF649594),
                            inactiveBorderColor = Color(0xFF649594)
                        )
                    ) {
                        Text(label, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        when (selectedIndex) {
            0 -> StatisticsScreen(viewModel = viewModel, navController = navController)
            1 -> LineChartScreen(viewmodel = viewModel)
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