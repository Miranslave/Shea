package com.example.myapplication.firestoredb.data

import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    val db = Firebase.firestore

    fun getUserWebtoonFolders(uid: String, callback: FirestoreCallback<List<WebtoonFolder>>) {
        val res = ArrayList<WebtoonFolder>()
        db.collection("WebtoonFolder").whereEqualTo("uid", uid).get().addOnSuccessListener { result ->
            for (document in result) {
                val folder = WebtoonFolder(document.data.get("title").toString(), document.data.get("description").toString(), document.id)
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
        db.collection("Favorite").whereEqualTo("uid", userId).get().addOnSuccessListener { result ->
            // No favorite folder found
            if (result.isEmpty) {
                // Create a new favorites document
                val newFavorites = hashMapOf(
                    "uid" to userId,
                    "webtoonsid" to arrayListOf<Long>(),
                )

                db.collection("Favorite").add(newFavorites).addOnSuccessListener { documentReference ->
                    val folder = WebtoonFolder("Favoris", "Les webtoons que vous avez ajouté en favoris", documentReference.id)
                    callback.onSuccess(folder)
                }.addOnFailureListener { exception ->
                    Log.w("Failed", "Error while creating user favorites folder", exception)
                    callback.onError(Exception("Error while creating user favorites folder"))
                }
            } else {
                // Favorite folder found
                val document = result.documents[0]
                val folder = WebtoonFolder("Favorites", "Les webtoons que vous avez ajouté en favoris", document.id, false)
                val webtoonsIdList = document.data?.get("webtoonsid") as? ArrayList<Long>

                // Put the webtoons id in the folder
                for (i in 0..((webtoonsIdList?.size)?.minus(1) ?: 0)) {
                    webtoonsIdList?.get(i)?.let { folder.addWebtoon(it) }
                }

                callback.onSuccess(folder)
            }

        }.addOnFailureListener { exception ->
            Log.w("Failed", "Error while getting user webtoon folders", exception)
            callback.onError(Exception("Error while getting user webtoon folders"))
        }
    }
}