package com.example.myapplication

import android.content.ContentValues.TAG
import android.media.session.MediaSession.Token
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodeldeliverdata.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {


    lateinit var  viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val resultTextView: TextView = findViewById(R.id.textView)



        resultTextView.text = viewModel._status.toString()
        viewModel.status.observe(this, Observer { resultTextView.text= it.toString()})
        request()
    }

    private fun request(){
        val bouton: Button = findViewById(R.id.button)
        bouton.setOnClickListener{

        }
    }

   //Fonction basique qui permet d'appeler l'API en dur pour obtenir le JSon du HOME


}







