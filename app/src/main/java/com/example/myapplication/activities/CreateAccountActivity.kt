package com.example.myapplication.activities

import android.content.ContentValues
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

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        lateinit var mail:String
        lateinit var pass:String

        val TologinButton = findViewById<TextView>(R.id.createAccountActivity_loginButton)
        TologinButton.setOnClickListener {
            showAccountCreationActivity()
        }
        val createAccountButton = findViewById<Button>(R.id.createAccountActivity_createAccountButton)
        createAccountButton.setOnClickListener {
            mail = findViewById<EditText>(R.id.createAccountActivity_mailAddress).text.toString()
            pass = findViewById<EditText>(R.id.createAccountActivity_password).text.toString()
            CreateAccount(mail,pass)
        }
    }

    private fun showAccountCreationActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun CreateAccount(mail:String,password:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "user:$mail",
                        Toast.LENGTH_SHORT,
                    ).show()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    showAccountCreationActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Inscription failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }
}
