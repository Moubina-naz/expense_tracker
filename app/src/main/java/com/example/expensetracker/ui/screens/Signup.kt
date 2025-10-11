package com.example.expensetracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.data.models.LoginScrn
import com.example.expensetracker.data.models.SignUpScrn
import com.example.expensetracker.viewmodels.SignupState
import com.example.expensetracker.viewmodels.SignupViewModel

@Composable
fun SignUpScreen(navController: NavController,viewModel: SignupViewModel) {
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val signupState by viewModel.signupState.collectAsState()

    // Handle navigation on success
    LaunchedEffect(signupState) {
        when (signupState) {
            is SignupState.Success -> {
                println("✅ Signup successful, navigating to home")
                navController.navigate(HomeScrn) {
                    popUpTo(SignUpScrn) { inclusive = true }
                }
            }
            is SignupState.Error -> {
                println("❌ Signup error: ${(signupState as SignupState.Error).message}")
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    .clickable { /* navController.popBackStack() */ }
            )

            // Title in the center
            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Show error if any
        if (signupState is SignupState.Error) {
            Text(
                text = (signupState as SignupState.Error).message,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
        }


        // FIXED: Each field uses its own variable
        InputField(
            label = "Username*",
            value = username,
            onValueChange = { username = it }
        )

        InputField(
            label = "First name*",
            value = firstName,
            onValueChange = { firstName = it }
        )

        InputField(
            label = "Last name*",
            value = lastName,
            onValueChange = { lastName = it }
        )

        InputField(
            label = "Email*",
            value = email,
            onValueChange = { email = it }
        )

        InputField(
            label = "Password*",
            value = password,
            onValueChange = { password = it },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            }
        )

        Button(
            onClick = {
                println("🔄 Signup button clicked")
                viewModel.signup(
                    username = username,
                    first_name = firstName,
                    last_name = lastName,
                    email = email,
                    password = password
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = signupState != SignupState.Loading
        ) {
            if (signupState == SignupState.Loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Sign up", color = Color.White)
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {var isClicked by remember { mutableStateOf(false) }
            Text("Already Have Account? ")
            Text(
                text = "Log In",
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                modifier=Modifier.clickable { isClicked = !isClicked
                    navController.navigate(LoginScrn) })

        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(label, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(label.removeSuffix("*")) },
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
   // SignUpScreen()
}