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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.CommentAdapter
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.example.myapplication.image.ImageLoader
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.example.myapplication.viewModels.WebtoonDetailsViewModel

// Define a Fragment to show the details of a Webtoon
class WebtoonDetailsFragment(private val webtoon: Webtoon) : Fragment(), BackButtonHandler {
    private lateinit var imageLoader: ImageLoader

    private val viewModel = WebtoonDetailsViewModel()

    // Inflate the layout for this fragment
    lateinit var recyclerView: RecyclerView
    lateinit var firebaseDocumentUID: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imageLoader = ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())
        return inflater.inflate(R.layout.fragment_webtoon_details, container, false)
    }

    // Set up the view after it has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show the details of the Webtoon
        this.showWebtoonDetails(view)

        // Check if the Webtoon is already in the database
        this.viewModel.isWebtoonInDatabase(webtoon) { isFound ->
            if (!isFound) {
                this.viewModel.createWebtoonCommentEntry(webtoon)
            }
        }

        // Set the onClickListener for the "Previous Page" button
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_previousPageButton).setOnClickListener { this.goBack() }

        // Set the onClickListener for the URL button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_urlButton).setOnClickListener { this.urlButtonClick() }

        // Set the onClickListener for the bookmark button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setOnClickListener { this.bookmarkButtonClick(view) }

        // Set the onClickListener for the favorite button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_favoriteButton).setOnClickListener { this.favoriteButtonClick(view) }


        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView = view.findViewById(R.id.fragmentWebtoonDetails_commentsList)
        recyclerView.layoutManager = layoutManager

        this.viewModel.getWebtoonComments(webtoon) { commentList ->
            val adapter = CommentAdapter(commentList)
            recyclerView.adapter = adapter
        }

        view.findViewById<Button>(R.id.fragmentWebtoonDetails_publishCommentButton).setOnClickListener {
            val commentText = view.findViewById<TextView>(R.id.fragmentWebtoonDetails_publishCommentText)

            this.viewModel.addWebtoonComment(webtoon, commentText.text.toString(), object : FirestoreCallback<Boolean> {
                override fun onSuccess(result: Boolean) {
                    commentText.text = ""
                    viewModel.getWebtoonComments(webtoon) { commentList ->
                        val adapter = CommentAdapter(commentList)
                        recyclerView.adapter = adapter
                    }
                    Toast.makeText(context, getString(R.string.comment_published), Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, getString(R.string.comment_error), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Show the details of the Webtoon
    private fun showWebtoonDetails(view: View) {
        // Change the title of the activity
        (activity as? BaseActivity)?.changeTitle(webtoon.getTitle())

        val saveButton = view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton)
        val favButton = view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_favoriteButton)

        // Set the author, description, genre, status, total episodes of the Webtoon
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_author).text = webtoon.getAuthor()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_description).text = webtoon.getSynopsis()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_genre).text = webtoon.getGenre()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_theme).text = webtoon.getTheme()
        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_totalEpisodes).text = webtoon.getTotalEpisodeCount().toString()
        imageLoader.load(view.findViewById(R.id.fragmentWebtoonDetails_imageView), webtoon.getThumbnail())

        // Change the bookmark icon if the Webtoon is already in the user's library
        saveButton.isEnabled = false
        this.viewModel.isWebtoonInBookmarks(webtoon, object : FirestoreCallback<Boolean> {
            override fun onSuccess(result: Boolean) {
                saveButton.isEnabled = true
                if (result) {
                    saveButton.setImageResource(R.drawable.bookmark_slash)
                }
            }

            override fun onError(e: Throwable) {
                saveButton.isEnabled = true
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })

        // Change the favorite icon if the Webtoon is already in the user's favorites
        favButton.isEnabled = false
        this.viewModel.isWebtoonInFavorites(webtoon, object : FirestoreCallback<Boolean> {
            override fun onSuccess(result: Boolean) {
                Log.i("WebtoonFavorites", "Webtoon ${webtoon.getId()} is already in the fav list : $result")
                favButton.isEnabled = true
                if (result) {
                    favButton.setImageResource(R.drawable.star_filled)
                }
            }

            override fun onError(e: Throwable) {
                favButton.isEnabled = true
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun goBack() {
        val baseActivity = (activity as? BaseActivity)
        baseActivity?.changeTitle(getString(R.string.library_tab_title))
        baseActivity?.changeFragment(LibraryFragment())
    }

    private fun favoriteButtonClick(view: View) {
        val favButton = view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_favoriteButton)
        favButton.isEnabled = false
        this.viewModel.isWebtoonInFavorites(webtoon, object : FirestoreCallback<Boolean> {
            override fun onSuccess(isAlreadyInFavorites: Boolean) {
                favButton.isEnabled = true

                if (isAlreadyInFavorites) {
                    favButton.setImageResource(R.drawable.star)
                    viewModel.setWebtoonAsFavorite(webtoon, false, object : FirestoreCallback<Boolean> {
                        override fun onSuccess(result: Boolean) {
                            Log.i("WebtoonFavorites", "Webtoon wasn't in list and is now in the fav list : $result")
                            Toast.makeText(context, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(context, getString(R.string.favorite_error), Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    favButton.setImageResource(R.drawable.star_filled)
                    viewModel.setWebtoonAsFavorite(webtoon, true, object : FirestoreCallback<Boolean> {
                        override fun onSuccess(result: Boolean) {
                            Log.i("WebtoonFavorites", "Webtoon was in list and is now in the fav list : $result")
                            Toast.makeText(context, getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(context, getString(R.string.favorite_error), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onError(e: Throwable) {
                favButton.isEnabled = true
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
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

        this.viewModel.getUserWebtoonFolders(object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                // If list is empty, display a message
                if (result.isEmpty()) {
                    Toast.makeText(context, getString(R.string.no_bookmarks_folder), Toast.LENGTH_SHORT).show()
                    return
                }

                val folderTitles = result.map { it.getTitle() }.toTypedArray()
                val folderIds = result.map { it.getDatabaseId() }
                val initialBookmarks = result.filter { folder -> folder.getWebtoons().any { it.getId() == webtoon.getId() } }.map { it.getDatabaseId() }.toMutableList()
                val finalBookmarks = initialBookmarks.toMutableList()
                val checkedItems = BooleanArray(result.size) { folderIds[it] in initialBookmarks }

                Log.i("WebtoonBookmarks", "------------")
                Log.i("WebtoonBookmarks", "Initial bookmarks: $initialBookmarks")
                Log.i("WebtoonBookmarks", "Folder ids: $folderIds")

                builder.setMultiChoiceItems(folderTitles, checkedItems) { _, which, isChecked ->
                    if (isChecked) {
                        finalBookmarks.add(folderIds[which])
                    } else {
                        finalBookmarks.remove(folderIds[which])
                    }
                    Log.i("WebtoonBookmarks", "Final bookmarks after change: $finalBookmarks")
                }

                builder.setPositiveButton(getString(R.string.save)) { _, _ ->
                    Log.i("WebtoonBookmarks", "------------")
                    Log.i("WebtoonBookmarks", "Initial bookmarks: ${initialBookmarks.sorted()}")
                    Log.i("WebtoonBookmarks", "Final bookmarks: ${finalBookmarks.sorted()}")
                    Log.i("WebtoonBookmarks", "Folder ids: $folderIds")
                    if (initialBookmarks.sorted() != finalBookmarks.sorted()) {
                        Log.i("WebtoonBookmarks", "Updating bookmarks")
                        viewModel.changeWebtoonBookmarks(webtoon, initialBookmarks, finalBookmarks, object : FirestoreCallback<Boolean> {
                            override fun onSuccess(result: Boolean) {
                                Log.i("WebtoonBookmarks", "Bookmarks updated")
                                Toast.makeText(context, getString(R.string.bookmark_updated), Toast.LENGTH_SHORT).show()

                                if (finalBookmarks.isNotEmpty()) {
                                    view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setImageResource(R.drawable.bookmark_slash)
                                } else {
                                    view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_saveButton).setImageResource(R.drawable.bookmark)
                                }
                            }

                            override fun onError(e: Throwable) {
                                Log.e("WebtoonBookmarks", "Error while updating bookmarks", e)
                                Toast.makeText(context, getString(R.string.bookmark_error), Toast.LENGTH_SHORT).show()
                            }
                        })
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
}
