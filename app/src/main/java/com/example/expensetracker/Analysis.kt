package com.example.expensetracker

import ChartCirclePie
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(

) {



    Column(modifier = Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(painter = painterResource(id = R.drawable.arrowbackios), contentDescription = "")


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
        Box(modifier = Modifier){


        }


    }
    }

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}