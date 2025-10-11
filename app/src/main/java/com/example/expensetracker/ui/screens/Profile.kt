package com.example.expensetracker.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.data.models.AllTransac
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.viewmodels.AuthViewModel
import com.example.expensetracker.viewmodels.ProfileViewModel
import com.example.expensetracker.viewmodels.Transacviewmodel
import com.example.expensetracker.viewmodels.UpdateState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Initialize fields when profile loads
    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            firstName = profile.first_name
            lastName = profile.last_name
            username = profile.username
            email = profile.email
        }
    }

    // Show success message
    LaunchedEffect(updateState) {
        if (updateState is UpdateState.Success) {
            navController.popBackStack()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
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

        // Error message
        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }

        // Profile Picture Section (optional)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { if (newPassword.isNotEmpty()) {
                        if (newPassword != confirmPassword) {
                            // Show error - passwords don't match
                            viewModel.clearMessages()
                            // You might want to set an error message here
                            return@Button
                        }
                        if (currentPassword.isEmpty()) {
                            // Show error - current password required
                            viewModel.clearMessages()
                            // You might want to set an error message here
                            return@Button
                        }
                        viewModel.changePassword(currentPassword, newPassword)
                    }

                        // Update profile information
                        viewModel.updateProfile(email, username, firstName, lastName) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.base),
                        contentColor = Color.White
                    )
                ) {
                    if (updateState == UpdateState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Save Changes", fontSize = 18.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Personal Information Section
        Text(
            text = "Personal Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileTextField(
            label = "First Name",
            value = firstName,
            onValueChange = { firstName = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(
            label = "Last Name",
            value = lastName,
            onValueChange = { lastName = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Change Password Section
        Text(
            text = "Change Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileTextField(
            label = "Current Password",
            value = currentPassword,
            onValueChange = { currentPassword = it },
            isPassword = true,
            placeholder = "Enter current password"
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(
            label = "New Password",
            value = newPassword,
            onValueChange = { newPassword = it },
            isPassword = true,
            placeholder = "Enter new password"
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField(
            label = "Confirm New Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
            placeholder = "Confirm new password"
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button


        Spacer(modifier = Modifier.height(50.dp))
    }
}


@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = singleLine,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default
    )
}

data class ProfileData(
    val firstName: String = "",
    val surname: String = "",
    val about: String = "",
    val email: String = "",
    val username: String = ""
)

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    /*EditProfileScreen(
        initialProfile = ProfileData(
            firstName = "Leroy",
            surname = "XOxO",
            about = "Tell your story",
            email = "https://",
            username = "leroy0646"
        ),
        onDone = {} // No-op lambda for preview
    )*/

}