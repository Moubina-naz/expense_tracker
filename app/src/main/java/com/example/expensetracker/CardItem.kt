package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardItem(modifier: Modifier){
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()
        .height(200.dp)
        .padding(20.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(colorResource(id = R.color.card))) {
        Box(modifier = Modifier
            .fillMaxWidth().weight(1f)
            .padding(16.dp)) {
            Column {
                Text(
                    text = "WELCOME NAZ",
                    fontSize = 18.sp,  // Use sp instead of dp for text size
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Image(painter = painterResource(id = R.drawable.menu_24), contentDescription =null,
                modifier = Modifier.align(Alignment.CenterEnd))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            cardrowitem(modifier = Modifier.weight(1F), title = "Balance", amount ="₹200" , icon = R.drawable.arrow_downward_24 )
            cardrowitem(modifier = Modifier, title = "Expense", amount ="₹100" , icon = R.drawable.arrow_upward_24 )


        }

    }
}
@Composable
fun cardrowitem(modifier: Modifier, title :String, amount:String, icon:Int) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp,fontWeight = FontWeight.Bold)

        }
        Text(
            text = amount,
            fontSize = 20.sp,  // Use sp instead of dp for text size
            color = Color.White
        )
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewCardItem(){
CardItem(modifier = Modifier)
}