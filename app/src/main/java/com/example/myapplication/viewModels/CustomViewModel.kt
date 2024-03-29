package com.example.myapplication.viewModels

import androidx.lifecycle.ViewModel
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.network.WebtoonApiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

// Custom ViewModel class that extends the ViewModel class
open class CustomViewModel : ViewModel() {
    val firestore: Firestore = Firestore()
    val db = Firebase.firestore
    val connectedUser = FirebaseAuth.getInstance().currentUser

    // Function to execute a given function in a coroutine scope
    fun <R> executeInCoroutineScope(function: KFunction<R>, params: List<Any>, callback: ViewModelCallback<R>) {
        // Launch a coroutine in the IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create a map of function parameters to their values
                val paramMap = function.parameters.zip(params).toMap()
                // Call the function with the parameter values
                val result = function.callSuspend(*paramMap.values.toTypedArray())

                // Switch to the Main dispatcher
                withContext(Dispatchers.Main) {
                    // Call the onSuccess method of the callback with the result
                    callback.onSuccess(result)
                }
            } catch (e: Exception) {
                // Switch to the Main dispatcher
                withContext(Dispatchers.Main) {
                    // Call the onError method of the callback with the exception
                    callback.onError(e)
                }
            }
        }
    }
}