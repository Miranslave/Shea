package com.example.myapplication.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var mail: String
    private lateinit var pass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if the user is already logged in
        val sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)

        if (isUserLoggedIn) {
            showHomeActivity()
            finish()
        }

        // Bind creation button
        val createAccountButton = findViewById<TextView>(R.id.activityLogin_createAccountButton)
        createAccountButton.setOnClickListener {
            showAccountCreationActivity()
        }

        // Bind connection button
        val loginButton = findViewById<Button>(R.id.activityLogin_connectionButton)
        loginButton.setOnClickListener {
            mail = findViewById<EditText>(R.id.activityLogin_mailAddress).text.toString()
            pass = findViewById<EditText>(R.id.activityLogin_password).text.toString()
            connect(mail, pass)
        }
    }

    private fun showAccountCreationActivity() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    private fun showHomeActivity() {
        val intent = Intent(this, BaseActivity::class.java)
        startActivity(intent)
    }

    private fun connect(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(baseContext, "Credentials missing", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Sign in sucess")

                // Save the connexion in shared preferences
                val sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isUserLoggedIn", true)
                editor.apply()

                showHomeActivity()
            } else {
                Log.w("TAG", "Sign in Failed", task.exception)
                Toast.makeText(baseContext, "Authentification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}