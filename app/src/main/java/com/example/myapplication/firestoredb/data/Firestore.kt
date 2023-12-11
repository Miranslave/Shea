package com.example.myapplication.firestoredb.data

import android.util.Log
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    private val db = Firebase.firestore

    fun getUserWebtoonFolders(uid: String, callback: FirestoreCallback<List<WebtoonFolder>>) {
        val res = ArrayList<WebtoonFolder>()
        db.collection("WebtoonFolder").whereEqualTo("uid", uid).get().addOnSuccessListener { result ->
            for (document in result) {
                val folder = WebtoonFolder(document.data["title"].toString(), document.data["description"].toString(), document.id)
                val webtoonsIdList = document.data["webtoonsid"] as? ArrayList<Long>

                // Add webtoons to the folder
                for (i in 0..((webtoonsIdList?.size)?.minus(1) ?: 0)) {
                    webtoonsIdList?.get(i)?.let { folder.addWebtoon(it) }
                }

                res.add(folder)
            }

            callback.onSuccess(res)
        }.addOnFailureListener { exception ->
            Log.w("Failed", "Error while getting user webtoon folders", exception)
            callback.onError(Exception("Error while getting user webtoon folders"))
        }
    }

    fun getUserFavoriteWebtoonsFolder(userId: String, callback: FirestoreCallback<WebtoonFolder>) {
        db.collection("Favorite").document(userId).get().addOnSuccessListener { document ->
            val webtoonsIdList = document.data?.get("favorites") as? ArrayList<*>
            val folder = WebtoonFolder("Favoris", "Les webtoons que vous avez ajouté en favoris", document.id, false)

            // No favorite folder found
            if (webtoonsIdList.isNullOrEmpty()) {
                // Create a new favorites document
                val newFavorites = hashMapOf(
                    "favorites" to arrayListOf<Long>(),
                )

                db.collection("Favorite").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).set(newFavorites)
                    .addOnSuccessListener {
                    callback.onSuccess(folder)
                }.addOnFailureListener { exception ->
                    Log.w("Failed", "Error while creating user favorites folder", exception)
                    callback.onError(Exception("Error while creating user favorites folder"))
                }
            } else {
                // Put the webtoons id in the folder
                for (i in 0..(webtoonsIdList.size).minus(1)) {
                    webtoonsIdList[i].let { folder.addWebtoon(it as Long) }
                }

                callback.onSuccess(folder)
            }

        }.addOnFailureListener { exception ->
            Log.w("Failed", "Error while getting user webtoon folders", exception)
            callback.onError(Exception("Error while getting user webtoon folders"))
        }
    }
}

