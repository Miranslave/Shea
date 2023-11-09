package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val createAccountButton = findViewById<TextView>(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            showAccountCreationActivity()
        }

        val loginButton = findViewById<Button>(R.id.connectionButton)
        loginButton.setOnClickListener {
            showHomeActivity()
        }
    }

    private fun showAccountCreationActivity() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    private fun showHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}