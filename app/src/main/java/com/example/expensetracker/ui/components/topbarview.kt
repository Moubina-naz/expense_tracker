package com.example.expensetracker.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbarview
            (title: String,
 onBackNavClicked :()-> Unit ={}
) {
    val navigationIcon : (@Composable () -> Unit)? = {
        if (!title.contains("Expense Tracker")) {
            val interactionSource = remember { MutableInteractionSource() }
            IconButton(onClick = { onBackNavClicked() }, interactionSource = interactionSource) {
                Icon(
                    //icon here was bacjk arrowyes k got it how did u fix the oreview it was never nroken thennnnnnnn? it needed to build to show as your project had errors so it couldnt do it hence the errorsss okay
                    imageVector = Icons.Default.Add,
                    contentDescription = "none",
                    tint = Color.White
                )
            }
        } else {
            null
        }


    }
    if (navigationIcon != null) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White, // Set text color
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .heightIn(max = 24.dp) ,
                    onTextLayout = {}
                )
            },

            navigationIcon = navigationIcon,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.base), // Background color
                titleContentColor = Color.White // Title text color
            )
        )
    }
}