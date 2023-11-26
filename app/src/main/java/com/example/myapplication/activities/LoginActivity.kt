package com.example.myapplication.activities

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        lateinit var mail:String
        lateinit var pass:String

        val createAccountButton = findViewById<TextView>(R.id.activityLogin_createAccountButton)
        createAccountButton.setOnClickListener {
            showAccountCreationActivity()
        }

        val loginButton = findViewById<Button>(R.id.activityLogin_connectionButton)
        loginButton.setOnClickListener {
            mail = findViewById<EditText>(R.id.activityLogin_mailAddress).text.toString()
            pass = findViewById<EditText>(R.id.activityLogin_password).text.toString()
            connect(mail,pass)
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
    private fun connect(email:String,password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task->
            if (task.isSuccessful){
                Log.d("TAG","Sign in sucess")
                showHomeActivity()
            }else{
                Log.w("TAG","Sign in Failed",task.exception)
                Toast.makeText(baseContext,"Authentification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}