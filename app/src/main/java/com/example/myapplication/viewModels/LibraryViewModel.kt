package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.Webtoon
import com.example.myapplication.enums.Langage
import com.example.myapplication.network.WebtoonOriginalsApi
import com.example.myapplication.network.originals.titleList.OriginalRequestTitleList
import com.example.myapplication.network.originals.titleList.OriginalTitle
import com.example.myapplication.network.originals.titleList.OriginalTitleList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Define a ViewModel for the Library
class LibraryViewModel : CustomViewModel() {
    private val db = Firebase.firestore

    // Define a list of Webtoon IDs
    private var webtoonsIdsList: List<Int> = listOf(75, 418, 676, 5727, 4940, 3485, 2467)

    // Function to get a list of Webtoons
    fun getWebtoonsList(callback: ViewModelCallback<List<Webtoon>>) {
        //webtoonsIdsList = dbgetuserreadingidlist("oNL8aBClglMLS7wujwKfUu86TsF3") // ne marche pas resultat vide
        executeInCoroutineScope(WebtoonOriginalsApi.retrofitService::getTitlesList, listOf(Langage.en), object : ViewModelCallback<OriginalRequestTitleList> {
            // Handle successful network request
            override fun onSuccess(result: OriginalRequestTitleList) {
                val titlesListMessage: OriginalTitleList = result.message.result.titleList

                if (titlesListMessage.titles.isEmpty()) {
                    callback.onError(Exception("Aucun résultat"))
                    return
                }

                val titlesList: List<OriginalTitle> = titlesListMessage.titles
                Log.i("info", "Titles list length " + titlesList.size.toString())

                // Extract array of infos from the list of titles with titles that correspond to the IDs in webtoonsIdsList
                val infosList = titlesList.filter {
                    webtoonsIdsList.contains(it.titleNo)
                }.map {
                    Webtoon(it.titleNo, it.title, it.synopsis, it.writingAuthorName, it.representGenre, it.theme, it.thumbnail, "https://www.webtoons.com/fr/osef/" + it.titleForSeo + "/list?title_no=" + it.titleNo, it.totalServiceEpisodeCount)
                }

                // Callback with a list of Webtoons
                callback.onSuccess(infosList)
            }

            // Handle error in network request
            override fun onError(e: Throwable) {
                // Catch IllegalStateExceptions because it means that the network request was successful but the response was not successfully shown
                // This is because the user changed the fragment before the response was shown
                if (e is IllegalStateException)
                    return

                callback.onError(e)
            }
        })
    }

    fun getDatabaseUserReadingList(uid: String): List<Int> {
        var res: List<Int> = emptyList()
        db.collection("UserData").get().addOnSuccessListener() { result ->
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
