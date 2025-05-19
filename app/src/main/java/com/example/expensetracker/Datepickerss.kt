package com.example.expensetracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import java.time.LocalDate
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WheelDate(
    showDatePicker: Boolean,
    onDismiss: () -> Unit,
    onSelectedDate: (String) -> Unit
){

    //var selectedDate by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    LaunchedEffect(isButtonClicked) {
        if (isButtonClicked) {
            delay(100)
            isButtonClicked = false
        }
    }
    if (showDatePicker){
        Dialog(onDismissRequest = onDismiss) {
            WheelDatePickerView(height = 180.dp,
                modifier = Modifier.fillMaxWidth(),
                showDatePicker = showDatePicker,
                dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
                rowCount = 3,
                yearsRange = 1920..LocalDate.now().year,
                title = "DUE DATE",
                titleStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ),
                dateTextColor = Color(0xff007AFF),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color.LightGray,
                ),
                showShortMonths = true,

                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 18.dp
                ),
                onDoneClick = {date->
                    //showDatePicker = false
                    isButtonClicked = true
                    val formattedDate = formatDateSlash(date.toString())
                    onSelectedDate(formattedDate)
                },
                onDismiss = onDismiss )

        }
    }
}

fun formatDateSlash(dateStr: String): String {
    val viewModel = Transacviewmodel()
    return try {
        // If already in correct format, return as-is
        if (dateStr.matches(Regex("""\d{2}/\d{2}/\d{4}"""))) {
            return dateStr
        }

        // Split by either - or /
        val parts = dateStr.split("-", "/")

        // Reformat based on input pattern
        when (parts.size) {
            3 -> {
                when {
                    // yyyy-MM-dd -> dd/MM/yyyy
                    dateStr.contains("-") -> "${parts[2]}/${parts[1]}/${parts[0]}"
                    // yyyy/MM/dd -> dd/MM/yyyy
                    else -> "${parts[2]}/${parts[1]}/${parts[0]}"
                }
            }
            else -> viewModel.getCurrentDate() // Fallback to current date
        }
    } catch (e: Exception) {
        viewModel.getCurrentDate() // Fallback to current date on error
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PickDate(label: String,
             value: String,
             onDateSelected: (String) -> Unit,
             viewModel : Transacviewmodel,
             modifier: Modifier = Modifier){
    var showPicker by remember { mutableStateOf(false) }
Column (modifier = Modifier.fillMaxWidth()){
    Box{
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(
                text = label,
                style = TextStyle(color = Color.Black),
                fontSize = 16.sp  // Explicitly set fontSize to resolve ambiguity
            )},
            modifier = modifier
                .fillMaxWidth(),

            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Select date",
                    modifier = Modifier. size(40.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showPicker = true },
        )

        if (showPicker) {
            WheelDate(
                showDatePicker = true,
                onDismiss = { showPicker = false },
                onSelectedDate = {
                    val formattedDate = formatDateSlash(it)
                    onDateSelected(formattedDate)

                    // Extract month and year reliably
                    val parts = formattedDate.split("/")
                    if (parts.size == 3) {
                        viewModel.selectMonth(parts[1], parts[2])
                    }
                    showPicker = false
                }
            )
        }
    }
}

}

