package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.example.myapplication.network.WebtoonApiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase

// Define a ViewModel for the Library
class WebtoonFolderDetailsViewModel : CustomViewModel() {
    private val webtoonApiController = WebtoonApiController.getInstance()

    // Function to get a list of Webtoons
    fun getWebtoonsList(filetingIdsList: List<Int>, callback: ViewModelCallback<List<Webtoon>>) {
        webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                callback.onSuccess(result.filter { filetingIdsList.contains(it.getId()) })
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun followFolder(folder : WebtoonFolder){
        db.collection("Follows").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .update("follows", FieldValue.arrayUnion(folder.getDatabaseId()))
            .addOnSuccessListener {
                Log.w("Error", "Succesfully followed")
            }
            .addOnFailureListener {e->
                Log.w("Error", "Failed to follow", e)
            }
    }
}
