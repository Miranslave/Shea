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
import com.google.firebase.auth.UserProfileChangeRequest

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val TologinButton = findViewById<TextView>(R.id.createAccountActivity_loginButton)
        TologinButton.setOnClickListener {
            showAccountCreationActivity()
        }
        val createAccountButton = findViewById<Button>(R.id.createAccountActivity_createAccountButton)
        createAccountButton.setOnClickListener {
            val mail = findViewById<EditText>(R.id.createAccountActivity_mailAddress).text.toString()
            val pass = findViewById<EditText>(R.id.createAccountActivity_password).text.toString()
            val username = findViewById<EditText>(R.id.createAccountActivity_username).text.toString()
            createAccount(mail, pass, username)
        }
    }

    private fun showAccountCreationActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun createAccount(mail: String, password: String, username:String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d(ContentValues.TAG, "User profile name updated.")
                        }
                    }

                Toast.makeText(baseContext, "L'inscription a réussi.", Toast.LENGTH_SHORT).show()

                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                showAccountCreationActivity()
            } else {
                Toast.makeText(baseContext, "L'incription a échoué.", Toast.LENGTH_SHORT).show()

                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
            }
        }
    }
}
