package com.example.myapplication.viewmodeldeliverdata

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.enums.Langage
import com.example.myapplication.network.WebtoonCanvasApi
import com.example.myapplication.network.WebtoonOriginalsApi
import com.example.myapplication.network.language
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {

    //initialise la fonction API qui va etre demander et la passe dans notre Viewmodel  une page -> un viewmodel
    init {
        ApiRequest()
    }

    val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status



    private fun ApiRequest() {
        viewModelScope.launch {
            try {
                val currentText = WebtoonOriginalsApi.retrofitService.getGenresList( Langage.en)
                Log.d("Success", currentText.toString())
                _status.value = "Success: $currentText"
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }
}