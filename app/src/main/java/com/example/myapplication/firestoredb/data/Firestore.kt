package com.example.myapplication.firestoredb.data

import android.annotation.SuppressLint
import android.util.Log
import com.example.myapplication.models.WebtoonFolder
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
                        val folder = WebtoonFolder(document.data.get("title").toString(),document.data.get("description").toString(),document.id)
                        val webtoonsIdList = document.data["webtoonsid"] as? ArrayList<Long>

                        for( i in 0..((webtoonsIdList?.size)?.minus(1) ?: 0)){
                            webtoonsIdList?.get(i)?.let { folder.addWebtoon(it) }
                        }

                        res.add(folder)
                    }
                }

                callback.onSuccess(res)
            }
            .addOnFailureListener { exception ->
                Log.w("Failed", "Error getting documents.", exception)
                callback.onError(Exception("Error from WebtoonFolder"))
            }
    }


}