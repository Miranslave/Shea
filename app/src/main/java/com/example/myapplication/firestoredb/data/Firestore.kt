package com.example.myapplication.firestoredb.data

import android.annotation.SuppressLint
import android.util.Log
import com.example.myapplication.WebtoonFolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore() {

    val  db = Firebase.firestore



    @SuppressLint("SuspiciousIndentation")
    fun WebtoonFolder(uid:String, callback: FirestoreCallback<List<WebtoonFolder>>){
        var res = ArrayList<Any>()
            db.collection("WebtoonFolder")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.get("uid")==uid){
                    res.add(WebtoonFolder(document.data.get("title").toString()
                        ,document.data.get("description").toString()))
                        }
                }


                //res = res.toList() as ArrayList<Any>
                callback.onSuccess(res)
            }
            .addOnFailureListener { exception ->
                Log.w("Failed", "Error getting documents.", exception)
                callback.onError(Exception("Error from WebtoonFolder"))
            }
    }
}