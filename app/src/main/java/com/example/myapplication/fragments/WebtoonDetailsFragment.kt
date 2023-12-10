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
    private lateinit var imageLoader: ImageLoader
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val firestore = Firestore()


    // Inflate the layout for this fragment
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
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_previousPageButton).setOnClickListener { this.backButtonClick() }

        // Set the onClickListener for the URL button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_urlButton).setOnClickListener { this.urlButtonClick() }

        // Set the onClickListener for the bookmark button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setOnClickListener { this.bookmarkButtonClick(view) }

        // Set the onClickListener for the favorite button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_favoriteButton).setOnClickListener { this.favoriteButtonClick(view) }
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

        // Change the bookmark icon if the Webtoon is already in the user's library
        firestore.WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<Any>) {
                result.forEach { doc ->
                    doc as WebtoonFolder

                    if (doc.getWebtoons().any { it.getId() == webtoon.getId() }) {
                        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setImageResource(R.drawable.bookmark_slash)
                    }
                }
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun backButtonClick() {
        activity?.onBackPressed()

//        val baseActivity = (activity as? BaseActivity)
//        baseActivity?.changeFragment(LibraryFragment())
//        baseActivity?.changeTitle(getString(R.string.library_tab_title))
    }

    private fun favoriteButtonClick(view: View) {
        // TODO: Add the Webtoon to the user's favorites
        // Show a toast message
        Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show()

        // Change the favorite icon
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_favoriteButton).setImageResource(R.drawable.star_filled)
    }

    private fun urlButtonClick() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(webtoon.getLinkUrl())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    private fun bookmarkButtonClick(view: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_to_folder))

        firestore.WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<Any>) {
                val folderInfo = result.map { it as WebtoonFolder }
                val folderTitles = folderInfo.map { it.getTitle() }.toTypedArray()
                val folderIds = folderInfo.map { it.getdbid() }
                val initialFavorites = folderInfo.filter { folder -> folder.getWebtoons().any { it.getId() == webtoon.getId() } }.map { it.getdbid() }.toMutableList()

                val checkedItems = BooleanArray(folderInfo.size) { folderIds[it] in initialFavorites }

                builder.setMultiChoiceItems(folderTitles, checkedItems) { _, which, isChecked ->
                    if (isChecked) {
                        initialFavorites.add(folderIds[which])
                    } else {
                        initialFavorites.remove(folderIds[which])
                    }
                }

                builder.setPositiveButton(getString(R.string.save)) { _, _ ->
                    val finalFavorites = initialFavorites.sorted()
                    if (folderIds.sorted() != finalFavorites) {
                        changeBookmarkFolders(folderIds, finalFavorites, firestore.db)
                        if (finalFavorites.isNotEmpty()) {
                            view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setImageResource(R.drawable.bookmark_slash)
                        }
                    }
                }

                builder.setNegativeButton(getString(R.string.abort)) { dialog, _ -> dialog.cancel() }

                val dialogWindow = builder.create()
                dialogWindow.show()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changeBookmarkFolders(arrayIdFavoriteStart: List<String>, arrayIdFavoriteEnd: List<String>, db: FirebaseFirestore) {
        val foldersToAdd = arrayIdFavoriteEnd.subtract(arrayIdFavoriteStart)
        val foldersToRemove = arrayIdFavoriteStart.subtract(arrayIdFavoriteEnd)

        foldersToAdd.forEach { folderId ->
            val updates = hashMapOf("webtoonsid" to FieldValue.arrayUnion(webtoon.getId()))
            db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener { Log.d("WebtoonFavoris", "DocumentSnapshot successfully updated!") }.addOnFailureListener { e -> Log.w("WebtoonFavoris", "Error updating document", e) }
        }

        foldersToRemove.forEach { folderId ->
            val updates = hashMapOf("webtoonsid" to FieldValue.arrayRemove(webtoon.getId()))
            db.collection("WebtoonFolder").document(folderId).update(updates as Map<String, Any>).addOnSuccessListener { Log.d("WebtoonFavoris", "DocumentSnapshot successfully updated!") }.addOnFailureListener { e -> Log.w("WebtoonFavoris", "Error updating document", e) }
        }
    }
}
