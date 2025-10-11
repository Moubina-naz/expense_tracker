package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R

@Composable
fun CurrencySelectionMinimal(navController: NavController= rememberNavController()) {
    val currencies = listOf("INR","USD", "EUR", "GBP", "PLN")
    var selected by remember { mutableStateOf("INR") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
            ) {
                // Back Arrow on the left
                Icon(
                    painter = painterResource(id = R.drawable.arrowbackios),
                    contentDescription = "Back",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {  navController.popBackStack() }
                )

                // Title in the center
                Text(
                    text = "Choose your Currency",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            currencies.forEach { currency ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = currency }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == currency,
                        onClick = { selected = currency }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$currency - ${currencyName(currency)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Continue Button at Bottom Center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Handle continue */ },
                modifier = Modifier.widthIn(min = 120.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.base))
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}

fun currencyName(code: String): String = when (code) {
    "INR" -> "Indian Rupee"
    "USD" -> "US Dollar"
    "EUR" -> "Euro"
    "GBP" -> "Pound Sterling"
    "PLN" -> "Zloty"
    else -> ""
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectionMinimalPreview() {
    CurrencySelectionMinimal()
}