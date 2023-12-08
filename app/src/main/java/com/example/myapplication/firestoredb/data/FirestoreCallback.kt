package com.example.myapplication.firestoredb.data

import com.example.myapplication.WebtoonFolder

interface FirestoreCallback<R> {
    // This method is called when the operation is successful
    // 'result' is the result of the operation
    fun onSuccess(result: List<Any>)

    // This method is called when the operation fails
    // 'e' is the exception thrown by the operation
    fun onError(e: Throwable)
}