package com.example.myapplication.viewmodeldeliverdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.Titles
import com.example.myapplication.network.WebtoonApi
import com.example.myapplication.network.WebtoonApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeViewModel : ViewModel(){

    init {
            getHome()
        }
    val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status


    private fun getHome(){
        viewModelScope.launch {
            try{
             val currentText = WebtoonApi.retrofitService.getHome()
            _status.value = "Success: ${currentText.size}"
            }
        catch (e: Exception){
            _status.value = "Failure: ${e.message}"
            }
        }
    }
}