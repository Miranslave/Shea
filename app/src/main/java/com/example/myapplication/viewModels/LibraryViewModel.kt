package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.Webtoon
import com.example.myapplication.network.WebtoonApiController
import com.google.android.play.integrity.internal.l
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// Define a ViewModel for the Library
class LibraryViewModel : CustomViewModel() {
    private val db = Firebase.firestore
    private val  userid= FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val webtoonApiController = WebtoonApiController.getInstance()

    // Define a list of Webtoon IDs
    private var webtoonsIdsList: List<Int> = listOf(75, 418, 676, 5727, 4940, 3485, 2467)


    // Function to get a list of Webtoons
    fun getWebtoonsList(callback: ViewModelCallback<List<Webtoon>>) {
       getDatabaseUserReadingList(object : FirestoreCallback<List<Int>>{
            override fun onSuccess(result1: List<Any>) {
                webtoonsIdsList = result1 as List<Int>
                this@LibraryViewModel.webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>>{
                    override fun onSuccess(result: List<Webtoon>) {
                        Log.d("Data fun pre get", webtoonsIdsList.toString())
                        callback.onSuccess(result.filter { webtoonsIdsList.contains(it.getId()) })
                    }
                    override fun onError(e: Throwable) {
                        TODO("Not yet implemented")
                        callback.onError(e)
                    }
                })
            }
            override fun onError(e: Throwable) {
                Log.d("tag",e.toString())
            }

        })
        /*webtoonsIdsList = WebtoonListid
        this.webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                Log.d("Data fun pre get", webtoonsIdsList.toString())
                callback.onSuccess(result.filter { webtoonsIdsList.contains(it.getId()) })
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
                callback.onError(e)
            }

        })*/




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

    fun testgetDatabaseUserReadingList():List<Int> {
        var res: List<Int> = emptyList()

            db.collection("UserData").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("uid") == userid) {
                        res = document.data.get("Read") as List<Int>
                        //Log.d("Data fun pre getted", document.data.get("Read").toString())

                    }
                }
            }
            // On error, log the error and show a toast message.
            Log.d("Data fun pre getted", res.toString())
            return res
        }


    fun getDatabaseUserReadingList( callback: FirestoreCallback<List<Int>> ) {
        var res: List<Int> = emptyList()
        fun onSuccess(result: List<Webtoon>) {
            db.collection("UserData").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["uid"] == userid) {
                        res = document.data["Read"] as List<Int>
                        Log.d("Data lecture", document.data["Read"].toString())
                    }
                    callback.onSuccess(res)
                }
        }

        // On error, log the error and show a toast message.
        fun onError(e: Throwable) {
            Log.w("Failed", "Error getting idtitlesfromuser.", e)
            callback.onError(Exception("Error from WebtoonFolder"))
        }


        }

    }
}

