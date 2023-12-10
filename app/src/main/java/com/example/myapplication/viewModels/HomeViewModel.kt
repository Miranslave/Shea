package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.models.WebtoonFolder

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

    fun getWebtoonFoldersList(uid: String, callback: ViewModelCallback<List<WebtoonFolder>>) {
        Firestore().WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<Any>) {
                webtoonFoldersList = result as List<WebtoonFolder>
                callback.onSuccess(webtoonFoldersList)
                webtoonCallbackList.forEach { it.onSuccess(webtoonFoldersList) }
                webtoonCallbackList = emptyList()
            }

            override fun onError(e: Throwable) {
                callback.onError(e)
                webtoonCallbackList.forEach { it.onError(e) }
                webtoonCallbackList = emptyList()
            }
        })
    }
}
