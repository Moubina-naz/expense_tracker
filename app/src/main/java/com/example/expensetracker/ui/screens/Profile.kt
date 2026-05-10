package com.example.expensetracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.viewmodels.ProfileViewModel
import com.example.expensetracker.viewmodels.UpdateState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val updateState by viewModel.updateState.collectAsState()

    val prefs = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val userPreferences = remember { com.example.expensetracker.data.models.UserPreferences(context) }

    var name by remember { mutableStateOf(userPreferences.getUserName()) }
    var age by remember { mutableStateOf(prefs.getString("user_age", "") ?: "") }
    var country by remember { mutableStateOf(prefs.getString("user_country", "") ?: "") }
    var currency by remember { mutableStateOf(userPreferences.getUserCurrency()) }
    var expanded by remember { mutableStateOf(false) }
    val currencies = com.example.expensetracker.utils.CurrencyManager.supportedCurrencies

    LaunchedEffect(updateState) {
        if (updateState is UpdateState.Success) {
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrowbackios),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )

            Text(
                text = "Edit Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Personal Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileTextField(label = "Name", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(label = "Age", value = age, onValueChange = { age = it })
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(label = "Country", value = country, onValueChange = { country = it })
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = "$currency - ${com.example.expensetracker.utils.CurrencyManager.getCurrencyName(currency)}",
                onValueChange = {},
                readOnly = true,
                label = { Text("Currency") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text("$selectionOption - ${com.example.expensetracker.utils.CurrencyManager.getCurrencyName(selectionOption)}") },
                        onClick = {
                            currency = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.updateProfileInfo(name, age, country, currency)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.base)
            ),
            enabled = updateState != UpdateState.Loading && name.isNotBlank() && age.isNotBlank() && country.isNotBlank() && currency.isNotBlank()
        ) {
            if (updateState == UpdateState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Update Profile", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}