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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WheelDate(
    showDatePicker: Boolean,
    onDismiss: () -> Unit,
    onSelectedDate: (String) -> Unit
){

    //var selectedDate by remember { mutableStateOf("") }


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
                    val formattedDate= formatDateSlash(date.toString())
                    onSelectedDate(formattedDate)
                    println("Done: $formattedDate")
                },
                onDismiss = onDismiss )
        }
    }
}
fun formatDateSlash(dateStr: String): String {
    return try {
        // If the date is already in "dd/MM/yyyy", return as-is
        if (dateStr.contains("/")) {
            return dateStr
        }
        // Otherwise, parse and reformat
        val parts = dateStr.split("-")
        if (parts.size == 3) {
            "${parts[0]}/${parts[1]}/${parts[2]}" // dd/MM/yyyy
        } else {
            dateStr // Fallback (shouldn't happen)
        }
    } catch (e: Exception) {
        dateStr // Fallback
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
                    val formatdate = formatDateSlash(it)
                    val parts = formatdate.split("/")
                    if(parts.size==3) {
                        val monthyear ="${parts[1]}/${parts[2]}"

                        onDateSelected(formatdate)
                        viewModel.selectMonth(parts[1],parts[2])
                    }
                    showPicker = false
                }
            )
        }
    }
}

}

