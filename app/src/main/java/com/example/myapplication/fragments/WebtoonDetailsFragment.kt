package com.example.myapplication.fragments

// Import necessary Android and project-specific classes
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.image.ImageLoader
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// Define a Fragment to show the details of a Webtoon
class WebtoonDetailsFragment(private val webtoon: Webtoon) : Fragment() {

    // Inflate the layout for this fragment
    private lateinit var imageLoader: ImageLoader
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imageLoader = ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())
        return inflater.inflate(R.layout.fragment_webtoon_details, container, false)
    }

    // Set up the view after it has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show the details of the Webtoon
        this.showWebtoonDetails(view)

        // Set the onClickListener for the "Previous Page" button
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_previousPageButton).setOnClickListener {
            val baseActivity = (activity as? BaseActivity)
            baseActivity?.changeFragment(LibraryFragment())
            baseActivity?.changeTitle(getString(R.string.library_tab_title))
        }
    }

    // Show the details of the Webtoon
    private fun showWebtoonDetails(view: View) {
        // Change the title of the activity
        (activity as? BaseActivity)?.changeTitle(webtoon.getTitle())

        // Set the author, description, genre, status, total episodes of the Webtoon
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_author).text = webtoon.getAuthor()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_description).text = webtoon.getSynopsis()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_genre).text = webtoon.getGenre()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_theme).text = webtoon.getTheme()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_totalEpisodes).text = webtoon.getTotalEpisodeCount().toString()
        val imgview = view.findViewById<ImageView>(R.id.fragmentWebtoonDetails_imageView)
        imageLoader.load(imgview, webtoon.getThumbnail())
        // Set the onClickListener for the URL button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_urlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(webtoon.getLinkUrl())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.add_to_folder))

            val firestore = Firestore()

            firestore.WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
                override fun onSuccess(result: List<Any>) {
                    // Folders info list
                    val arrayTitle = ArrayList<String>()
                    val arrayFolderId = ArrayList<String>()

                    // List to know which folders will contains the webtoon
                    val arrayIdFavoriteStart = ArrayList<String>()
                    val arrayIdFavoriteEnd = ArrayList<String>()

                    // List displayed at first display
                    val alreadyChecked = mutableListOf<Boolean>()

                    for (doc in result) {
                        doc as WebtoonFolder
                        arrayTitle.add(doc.getTitle())
                        arrayFolderId.add(doc.getdbid())
                        var check = false

                        val arrayAlreadyFavorite = doc.getWebtoons()

                        for (web in arrayAlreadyFavorite) {
                            if (web.getId() == webtoon.getId()) {
                                arrayIdFavoriteStart.add(doc.getdbid())
                                check = true
                                break;
                            }
                        }

                        alreadyChecked.add(check)
                    }

                    // Perform a copy to compare the two lists later
                    arrayIdFavoriteEnd.addAll(arrayIdFavoriteStart)
                    builder.setMultiChoiceItems(arrayTitle.toArray(arrayOf<String>()), alreadyChecked.toBooleanArray()) { dialog, which, isChecked ->

                        if (isChecked) arrayIdFavoriteEnd.add(arrayFolderId[which])
                        else arrayIdFavoriteEnd.remove(arrayFolderId[which])
                    }

                    // Ok button
                    builder.setPositiveButton(getString(R.string.add)) { _, _ ->
                        if (arrayIdFavoriteStart.sorted() != arrayIdFavoriteEnd.sorted()) changeFavoriteFolders(arrayIdFavoriteStart, arrayIdFavoriteEnd, firestore.db)
                    }

                    // Cancel button
                    builder.setNegativeButton(getString(R.string.abort)) { dialog, _ -> dialog.cancel() }

                    // Build and show the window
                    val dialogWindow = builder.create()
                    dialogWindow.show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun changeFavoriteFolders(
        arrayIdFavoriteStart: ArrayList<String>, arrayIdFavoriteEnd: ArrayList<String>, db: FirebaseFirestore,
    ) {
        //Ajouts dans des folders
        for (folderId in arrayIdFavoriteEnd) {
            if (!arrayIdFavoriteStart.contains(folderId)) {

                val updates = hashMapOf(
                    "webtoonsid" to FieldValue.arrayUnion(webtoon.getId())
                )

                // Met à jour le champ "webtoonsid" avec le nouvel élément ajouté
                db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener { Log.d("WebtoonFavoris", "DocumentSnapshot successfully updated!") }.addOnFailureListener { e -> Log.w("WebtoonFavoris", "Error updating document", e) }
            }
        }

        FirebaseFirestore.getInstance()

        //Suppression dans des folders
        for (folderId in arrayIdFavoriteStart) {
            if (!arrayIdFavoriteEnd.contains(folderId)) {
                val updates = hashMapOf(
                    "webtoonsid" to FieldValue.arrayRemove(webtoon.getId())
                )

                // Met à jour le champ "webtoonsid" en supprimant l'élément spécifié
                db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener {
                        Log.d("WebtoonFavoris", "DocumentSnapshot successfully updated!")
                    }.addOnFailureListener { e ->
                        Log.w("WebtoonFavoris", "Error updating document", e)
                    }
            }
        }
    }
}
