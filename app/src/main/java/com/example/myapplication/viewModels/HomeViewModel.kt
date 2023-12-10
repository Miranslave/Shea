package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.content.ContentValues
import android.util.Log
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Define a ViewModel for the Library
class HomeViewModel : CustomViewModel() {
    private var webtoonCallbackList: List<ViewModelCallback<List<WebtoonFolder>>> = emptyList()
    private var webtoonFoldersList: List<WebtoonFolder> = emptyList()
    private val db: FirebaseFirestore = Firebase.firestore
    val connectedUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    // Function to search for a Webtoon in the list
    fun searchForWebtoonFolder(webtoonTitle: String, callback: ViewModelCallback<List<WebtoonFolder>>) {
        if (webtoonFoldersList.isNotEmpty()) {
            callback.onSuccess(webtoonFoldersList.filter { it.getTitle().contains(webtoonTitle, ignoreCase = true) })
        } else {
            webtoonCallbackList += callback
        }
    }

    fun getWebtoonFoldersList(callback: ViewModelCallback<List<WebtoonFolder>>) {
        Firestore().getUserWebtoonFolders(connectedUserId, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                webtoonFoldersList = result

                Firestore().getUserFavoriteWebtoonsFolder(connectedUserId, object : FirestoreCallback<WebtoonFolder> {
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

    fun addFolderToDatabase(title: String) {
        val webtoonFolder = hashMapOf("uid" to connectedUserId, "title" to title, "description" to "", "webtoonsid" to arrayListOf<Int>())

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
}
