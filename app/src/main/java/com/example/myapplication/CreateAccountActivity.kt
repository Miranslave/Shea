package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val createAccountButton = findViewById<TextView>(R.id.loginButton)
        createAccountButton.setOnClickListener {
            showAccountCreationActivity()
        }
    }

    private fun showAccountCreationActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
