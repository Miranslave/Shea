package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.models.Webtoon
import com.example.myapplication.network.WebtoonApiController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Define a ViewModel for the Library
class LibraryViewModel : CustomViewModel() {
    private val db = Firebase.firestore
    private val webtoonApiController = WebtoonApiController.getInstance()

    // Define a list of Webtoon IDs
    private var webtoonsIdsList: List<Int> = listOf(75, 418, 676, 5727, 4940, 3485, 2467)

    // Function to get a list of Webtoons
    fun getWebtoonsList(callback: ViewModelCallback<List<Webtoon>>) {
        this.webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                callback.onSuccess(result.filter { webtoonsIdsList.contains(it.getId()) })
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }

    // Function to search for a Webtoon in the list
    fun searchForWebtoon(webtoonTitle: String, callback: ViewModelCallback<List<Webtoon>>) {
        this.webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            override fun onSuccess(result: List<Webtoon>) {
                callback.onSuccess(result.filter { it.getTitle().contains(webtoonTitle, ignoreCase = true) })
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun getDatabaseUserReadingList(uid: String): List<Int> {
        var res: List<Int> = emptyList()
        db.collection("UserData").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data.get("uid") == uid) {
                    res = document.data.get("Read") as List<Int>

                    Log.d("Data lecture", document.data.get("Read").toString())
                }

            }

        }
        return res
    }
}
