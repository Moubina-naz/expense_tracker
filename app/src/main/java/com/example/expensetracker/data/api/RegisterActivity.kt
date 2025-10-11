package com.example.expensetracker.data.api

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.MainActivity
import com.example.expensetracker.R
import com.example.expensetracker.viewmodels.AuthViewModel
/*
class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val password = etPassword.text.toString()

            viewModel.registerUser(email, username, firstName, lastName, password)
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { user ->
                // Registration successful!
                Toast.makeText(this, "Welcome ${user.first_name}!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
/*/