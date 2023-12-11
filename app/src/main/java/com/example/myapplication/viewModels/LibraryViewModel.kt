package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.example.myapplication.network.WebtoonApiController

// Define a ViewModel for the Library
class LibraryViewModel : CustomViewModel() {
    private val webtoonApiController = WebtoonApiController.getInstance()

    // Function to get a list of Webtoons
    fun getWebtoonsList(callback: ViewModelCallback<List<Webtoon>>) {
        firestore.getUserFavoriteWebtoonsFolder(this.connectedUser?.uid.toString(), object : FirestoreCallback<WebtoonFolder> {
            override fun onSuccess(result: WebtoonFolder) {
                val webtoonsIdsList = result.getWebtoons().map { it.getId() }

                webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
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
}
