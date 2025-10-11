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
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.data.models.HomeScrn
import com.example.expensetracker.data.models.LoginScrn
import com.example.expensetracker.data.models.SignUpScrn
import com.example.expensetracker.viewmodels.LoginState
import com.example.expensetracker.viewmodels.LoginViewModel
import com.example.expensetracker.viewmodels.SignupViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    var username by remember { mutableStateOf("joe.doe@gmail.com") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberInfo by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()


    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                println("✅ Login successful, navigating to home")
                navController.navigate(HomeScrn) {
                    popUpTo(LoginScrn) { inclusive = true }
                }
            }
            is LoginState.Error -> {
                println("❌ Login error: ${(loginState as LoginState.Error).message}")
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
          
            Icon(
                painter = painterResource(id = R.drawable.arrowbackios),
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )

            Column {
                Text(
                    text = "Log In",
                    fontSize = 24.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.size(8.dp))
            }


            IconButton(onClick = { /* Handle options */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_horiz),
                    contentDescription = "More Options"
                )
            }
        }

        Text("Log in with one of the following", fontSize = 14.sp)

        // Show error if any
        if (loginState is LoginState.Error) {
            Text(
                text = (loginState as LoginState.Error).message,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
        }


        InputField(
            label = "Username *",
            value = username,
            onValueChange = { username = it },
            trailingIcon = {
                Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = Color.Green)
            }
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberInfo, onCheckedChange = { rememberInfo = it })
                Spacer(modifier = Modifier.width(4.dp))
                Text("Remember info")
            }
            Text(
                text = "Forgot Password",
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold
            )
        }


        Button(
            onClick = {
                viewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState != LoginState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            if (loginState == LoginState.Loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Log In", color = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("First time here? ")
            Text(
                text = "Sign up for free",
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(SignUpScrn)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
   // LoginScreen()
}