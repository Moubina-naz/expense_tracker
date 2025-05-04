package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.expensetracker.Room.CategoryItem

@Composable
fun CategoryGrid(
    cats: List<CategoryItem>,

    onCategorySelected: (CategoryItem) -> Unit
) {
    // Debug print to verify data

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // More reliable than Adaptive
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 400.dp)
            .background(Color.White) // Ensure background
    ) {
        items(cats) { item ->
            // Debug border to verify item bounds
            Box(modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Red) // Debug border
                .aspectRatio(0.9f)
                .clickable { onCategorySelected(item) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Debug: Show if icon is missing
                    if (item.iconRes == 0) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(Color.Red))
                    } else {
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.name,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.name,
                        color = Color.Black,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )


                }
            }
        }
    }
}