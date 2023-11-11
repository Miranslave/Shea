package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val createAccountButton = findViewById<TextView>(R.id.createAccountActivity_loginButton)
        createAccountButton.setOnClickListener {
            showAccountCreationActivity()
        }
    }

    private fun showAccountCreationActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
