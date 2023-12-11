package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.auth.FirebaseAuth

// Define a ViewModel for the Library
class HomeViewModel : CustomViewModel() {
    private var webtoonCallbackList: List<ViewModelCallback<List<WebtoonFolder>>> = emptyList()
    private var webtoonFoldersList: List<WebtoonFolder> = emptyList()

    // Function to search for a Webtoon in the list
    fun searchForWebtoonFolder(webtoonTitle: String, callback: ViewModelCallback<List<WebtoonFolder>>) {
        if (webtoonFoldersList.isNotEmpty()) {
            callback.onSuccess(webtoonFoldersList.filter { it.getTitle().contains(webtoonTitle, ignoreCase = true) })
        } else {
            webtoonCallbackList += callback
        }
    }

    fun getPersonalWebtoonFoldersList(callback: ViewModelCallback<List<WebtoonFolder>>) {
        Firestore().getUserWebtoonFolders(connectedUser?.uid.toString(), object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                webtoonFoldersList = result

                // Get the user favorite webtoons folder
                Firestore().getUserFavoriteWebtoonsFolder(connectedUser?.uid.toString(), object : FirestoreCallback<WebtoonFolder> {
                    override fun onSuccess(result: WebtoonFolder) {
                        webtoonFoldersList += result

                        callback.onSuccess(webtoonFoldersList)
                        webtoonCallbackList.forEach { it.onSuccess(webtoonFoldersList) }
                        webtoonCallbackList = emptyList()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("FAVORITE", "Error while getting user favorite webtoons folder")
                        callback.onError(e)
                        webtoonCallbackList.forEach { it.onError(e) }
                        webtoonCallbackList = emptyList()
                    }
                })
            }

            override fun onError(e: Throwable) {
                Log.d("WEBTOON", "Error while getting user webtoon folders")
                callback.onError(e)
                webtoonCallbackList.forEach { it.onError(e) }
                webtoonCallbackList = emptyList()
            }
        })
    }

    fun getWebtoonSharedFolderList(callback: ViewModelCallback<List<WebtoonFolder>>) {
        val res = ArrayList<WebtoonFolder>()
        db.collection("WebtoonFolder").get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.data["uid"] != FirebaseAuth.getInstance().currentUser?.uid && document.data["permission"].toString() == "public") {
                    val folder = WebtoonFolder(document.data["title"].toString(), document.data["description"].toString(), document.id, document.data["uid"].toString(), true)
                    val webtoonsIdList = document.data["webtoonsid"] as? ArrayList<Long>

                    // Add webtoons to the folder
                    for (i in 0..((webtoonsIdList?.size)?.minus(1) ?: 0)) {
                        webtoonsIdList?.get(i)?.let { folder.addWebtoon(it) }
                    }

                    res.add(folder)
                }
            }

            callback.onSuccess(res)
        }.addOnFailureListener { exception ->
            Log.w("Failed", "Error while getting public webtoon folders", exception)
            callback.onError(Exception("Error while getting public webtoon folders"))
        }
    }

    fun getWebtoonFollowedFolderList(callback: ViewModelCallback<List<WebtoonFolder>>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        db.collection("Follows").document(userId).get()
            .addOnSuccessListener { document ->
                val folders = document.data?.get("follows") as? ArrayList<String>

                // No favorite folder found
                if (folders.isNullOrEmpty()) {
                    // Create a new favorites document
                    val newFollows = hashMapOf(
                        "follows" to arrayListOf<Long>(),
                    )

                    db.collection("Follows").document(userId).set(newFollows).addOnSuccessListener {
                        callback.onSuccess(ArrayList<WebtoonFolder>())
                    }.addOnFailureListener { exception ->
                        Log.w("Failed", "Error while creating user follows document", exception)
                        callback.onError(Exception("Error while creating user follows document"))
                    }
                } else {
                    getAllFolders(folders, object : ViewModelCallback<List<WebtoonFolder>>{
                        override fun onSuccess(result: List<WebtoonFolder>) {
                            callback.onSuccess(result)
                        }

                        override fun onError(e: Throwable) {
                            Log.d("Webtoon followed folders fetch error", e.toString())
                        }
                    })
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Failed", "Error while getting user webtoon folders", exception)
                callback.onError(Exception("Error while getting user webtoon folders"))
            }
    }

    fun addFolderToDatabase(title: String, description: String, permission: String) {
        val webtoonFolder = hashMapOf(
            "uid" to connectedUser?.uid.toString(), "title" to title, "description" to description, "permission" to permission, "webtoonsid" to arrayListOf<Int>()
        )

        db.collection("WebtoonFolder").document().set(webtoonFolder).addOnSuccessListener {
            Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error writing document", e)
        }
    }

    fun deleteFolderFromDatabase(databaseIdsList: List<WebtoonFolder>) {
        databaseIdsList.filter {
            it.canBeDeleted()
        }.forEach {
            db.collection("WebtoonFolder").document(it.getDatabaseId()).delete()
        }
    }

    fun changeFolderInfo(title: String, description: String, dbid: String) {
        val updates = hashMapOf(
            "title" to title, "description" to description
        )

        db.collection("WebtoonFolder").document(dbid).update(updates as Map<String, Any>).addOnSuccessListener {
            Log.d("WebtoonFolder", "Title and description successfully changed")
        }.addOnFailureListener { e ->
            Log.d("WebtoonFolder", "Title and description change failed", e)
        }
    }

    fun getAllFolders(folderUidList : ArrayList<String>, callback: ViewModelCallback<List<WebtoonFolder>> ){
        val res = ArrayList<WebtoonFolder>()
        db.collection("WebtoonFolder").get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (folderUidList.contains(document.id) ) {
                    val folder = WebtoonFolder(document.data["title"].toString(), document.data["description"].toString(), document.id, document.data["uid"].toString(), true)
                    val webtoonsIdList = document.data["webtoonsid"] as? ArrayList<Long>

                    // Add webtoons to the folder
                    for (i in 0..((webtoonsIdList?.size)?.minus(1) ?: 0)) {
                        webtoonsIdList?.get(i)?.let { folder.addWebtoon(it) }
                    }

                    res.add(folder)
                }
            }

            callback.onSuccess(res)
        }.addOnFailureListener { exception ->
            Log.w("Failed", "Error while getting public webtoon folders", exception)
            callback.onError(Exception("Error while getting public webtoon folders"))
        }
    }
}
