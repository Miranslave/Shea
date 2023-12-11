package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.firestoredb.data.Comment
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// Define a ViewModel for the Library
class WebtoonDetailsViewModel : CustomViewModel() {

    fun isWebtoonInDatabase(webtoon: Webtoon, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Webtoon").get().addOnSuccessListener { result ->
            val found = result.any { document -> document.data.get("id").toString() == webtoon.getId().toString() }
            callback(found)
        }.addOnFailureListener { exception ->
            Log.w("WEBTOONCOMMENT", "Error getting documents.", exception)
            callback(false)
        }
    }

    fun getWebtoonComments(webtoon: Webtoon, callback: (List<Comment>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Webtoon").get().addOnSuccessListener { result ->
            val comments = result.filter { document -> document.data["id"].toString() == webtoon.getId().toString() }.flatMap { document ->
                val comments = document.data["comments"] as ArrayList<*>

                comments.map { comment ->
                    comment as HashMap<*, *>
                    Comment(comment["username"] as String, comment["comment"] as String, comment["time"] as Timestamp)
                }
            }
            callback(comments)
        }.addOnFailureListener { exception ->
            Log.w("WEBTOONCOMMENT", "Error getting documents.", exception)
        }
    }

    fun isWebtoonInFavorites(webtoon: Webtoon, callback: FirestoreCallback<Boolean>) {
        val databaseDocument = db.collection("Favorite").document(this.connectedUser?.uid.toString())
        databaseDocument.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val favorites = document.data?.get("favorites") as List<Long>
                callback.onSuccess(favorites.contains(webtoon.getId().toLong()))
            } else {
                this.createWebtoonFavoritesEntry(webtoon, object: FirestoreCallback<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        callback.onSuccess(false)
                    }

                    override fun onError(e: Throwable) {
                        callback.onError(e)
                    }
                })
            }
        }.addOnFailureListener { exception ->
            Log.e("WebtoonFavorites", "Fail checking document existence", exception)
            callback.onError(exception)
        }
    }

    private fun createWebtoonFavoritesEntry(webtoon: Webtoon, callback: FirestoreCallback<Boolean>) {
        val newFavorites = hashMapOf(
            "favorites" to arrayListOf(webtoon.getId().toString()),
        )

        db.collection("Favorite").document(this.connectedUser?.uid.toString()).set(newFavorites).addOnSuccessListener {
            Log.i("WebtoonFavorites", "Success creating document and adding favorite")
            callback.onSuccess(true)
        }.addOnFailureListener {
            Log.e("WebtoonFavorites", "Fail creating document and adding favorite", it)
            callback.onError(it)
        }
    }

    fun isWebtoonInBookmarks(webtoon: Webtoon, callback: FirestoreCallback<Boolean>) {
        firestore.getUserWebtoonFolders(this.connectedUser?.uid.toString(), object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                result.forEach { doc ->
                    if (doc.getWebtoons().any { it.getId() == webtoon.getId() }) {
                        callback.onSuccess(true)
                    }
                }
                callback.onSuccess(false)
            }

            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun changeWebtoonBookmarks(webtoon: Webtoon, arrayIdBookmarksStart: List<String>, arrayIdBookmarksEnd: List<String>, callback: FirestoreCallback<Boolean>) {
        val foldersToAdd = arrayIdBookmarksEnd.subtract(arrayIdBookmarksStart)
        val foldersToRemove = arrayIdBookmarksStart.subtract(arrayIdBookmarksEnd)

        Log.d("WebtoonBookmarks", "foldersToAdd: $foldersToAdd")
        foldersToAdd.forEach { folderId ->
            val updates = hashMapOf("webtoonsid" to FieldValue.arrayUnion(webtoon.getId()))
            this.db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener {
                Log.d("WebtoonBookmarks", "DocumentSnapshot successfully updated!")
            }.addOnFailureListener { e ->
                Log.w("WebtoonBookmarks", "Error updating document", e)
            }
        }

        Log.d("WebtoonBookmarks", "foldersToRemove: $foldersToRemove")
        foldersToRemove.forEach { folderId ->
            val updates = hashMapOf("webtoonsid" to FieldValue.arrayRemove(webtoon.getId()))
            this.db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener {
                Log.d("WebtoonBookmarks", "DocumentSnapshot successfully updated!")
            }.addOnFailureListener { e ->
                Log.w("WebtoonBookmarks", "Error updating document", e)
            }
        }

        callback.onSuccess(true)
    }

    fun createWebtoonCommentEntry(webtoon: Webtoon) {
        val newWebtoonComment = hashMapOf(
            "comments" to arrayListOf<Map<String, Any>>(), "id" to webtoon.getId()
        )

        db.collection("Webtoon").add(newWebtoonComment).addOnSuccessListener { documentReference ->
            Log.d("WebtoonDetails", "Nouvel objet commentaire dans la bdd")
        }.addOnFailureListener { e ->
            Log.d("WebtoonDetails", "Echec de la création de l'objet commentaire dans la bdd")
        }
    }

    fun addWebtoonComment(webtoon: Webtoon, comment: String, callback: FirestoreCallback<Boolean>) {
        db.collection("Webtoon").whereEqualTo("id", webtoon.getId()).get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                createWebtoonCommentEntry(webtoon)
            }

            val webtoonDocumentReference = documents.documents[0].id
            val currentTimeStamp = Timestamp.now()
            val databaseDocument = db.collection("Webtoon").document(webtoonDocumentReference)
            val newComment = hashMapOf(
                "comment" to comment,
                "username" to this.connectedUser?.displayName as String,
                "time" to currentTimeStamp
            )

            databaseDocument.update("comments", FieldValue.arrayUnion(newComment)).addOnSuccessListener {
                Log.w("WEBTOONCOMMENT", "Success adding comment")
                callback.onSuccess(true)
            }.addOnFailureListener {
                Log.w("WEBTOONCOMMENT", "Fail adding comment", it)
                callback.onError(it)
            }
        }.addOnFailureListener { exception ->
            Log.w("WEBTOONCOMMENT", "Error getting documents.", exception)
            callback.onError(exception)
        }
    }

    fun getUserWebtoonFolders(callback: FirestoreCallback<List<WebtoonFolder>>) {
        firestore.getUserWebtoonFolders(this.connectedUser?.uid.toString(), object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                callback.onSuccess(result)
            }

            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun setWebtoonAsFavorite(webtoon: Webtoon, addToFavorites: Boolean, callback: FirestoreCallback<Boolean>) {
    val databaseDocument = db.collection("Favorite").document(this.connectedUser?.uid.toString())
    databaseDocument.get().addOnSuccessListener { document ->
        if (document.exists()) {
            // Document exists, use it to add or remove the webtoon from the favorites list
            if (addToFavorites) {
                databaseDocument.update("favorites", FieldValue.arrayUnion(webtoon.getId())).addOnSuccessListener {
                    Log.i("WebtoonFavorites", "Success adding favorite")
                    callback.onSuccess(true)
                }.addOnFailureListener {
                    Log.e("WebtoonFavorites", "Fail adding favorite", it)
                    callback.onError(it)
                }
            } else {
                databaseDocument.update("favorites", FieldValue.arrayRemove(webtoon.getId())).addOnSuccessListener {
                    Log.i("WebtoonFavorites", "Success removing favorite")
                    callback.onSuccess(true)
                }.addOnFailureListener {
                    Log.e("WebtoonFavorites", "Fail removing favorite", it)
                    callback.onError(it)
                }
            }
        } else {
            this.createWebtoonFavoritesEntry(webtoon, object: FirestoreCallback<Boolean> {
                override fun onSuccess(result: Boolean) {
                    callback.onSuccess(false)
                }

                override fun onError(e: Throwable) {
                    callback.onError(e)
                }
            })
        }
    }.addOnFailureListener { exception ->
        Log.e("WebtoonFavorites", "Fail checking document existence", exception)
        callback.onError(exception)
    }
}
}
