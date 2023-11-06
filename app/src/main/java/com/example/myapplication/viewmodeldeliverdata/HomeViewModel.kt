package com.example.myapplication.viewmodeldeliverdata

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.WebtoonApi
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel(){

    init {
            getHome()
        }
    val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status


    @SuppressLint("SuspiciousIndentation")
    private fun getHome(){
        viewModelScope.launch {
            try{
             val currentText = WebtoonApi.retrofitService.getSearch()
                Log.d("TEST",currentText.toString())
            _status.value = "Success: $currentText"
            }
        catch (e: Exception){
            _status.value = "Failure: ${e.message}"
            }
        }
    }
}