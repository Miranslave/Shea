package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.Webtoon
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.viewModels.LibraryViewModel
import com.example.myapplication.viewModels.ViewModelCallback
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

// This class represents a fragment in the application that displays a list of webtoons in a RecyclerView.
class LibraryFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    // Initialize the ViewModel
    private var viewModel = LibraryViewModel()

    // This method inflates the layout for this fragment and initializes the RecyclerView with a grid layout to display folders in 2 columns.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val  user = FirebaseAuth.getInstance().currentUser
        var view = inflater.inflate(R.layout.fragment_library, container, false)
        this.initRecyclerViewDisplay(view, R.id.fragmentLibrary_itemsList, WebtoonsListAdapter(listOf<Webtoon>(), this, R.layout.item_library_webtoon), LinearLayoutManager(context))
        return view
    }

    // This method is called after the view is created. It fetches the list of webtoons from the `LibraryViewModel` and sets the content of the RecyclerView.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate the spinner
        this.animateSpinner()
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header("Referer", "http://m.webtoons.com/").addHeader("User-Agent","Mozilla/5.0 (Linux; Android 8.1.0; Mi MIX 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Mobile Safari/537.36")
                        .build()
                )
            }
            .build()

        val Picasso = Picasso.Builder(requireActivity().baseContext) //.downloader(client.)

        // Fetch the list of webtoons from the ViewModel
        viewModel.getWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                Log.d("Success", result.toString())
                setRecyclerViewContent(WebtoonsListAdapter(result, this@LibraryFragment, R.layout.item_library_webtoon))
                stopSpinner()
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                Log.d("Error", e.toString())
                Toast.makeText(context, "Une erreur empêche l'affichage", Toast.LENGTH_SHORT).show()
                stopSpinner()
            }
        })
    }

    // This method is called when an item in the RecyclerView is clicked. It changes the title of the main activity and replaces the current fragment with a `WebtoonDetailsFragment` that shows the details of the clicked webtoon.
    override fun onItemClick(position: Int, item: Any?) {
        val mainActivity = (activity as? BaseActivity)
        val webtoon = item as Webtoon
        mainActivity?.changeTitle(webtoon.getTitle())
        mainActivity?.changeFragment(WebtoonDetailsFragment(webtoon))
    }

    // This method is called when an item in the RecyclerView is drawn. It sets the title and synopsis of the webtoon on the corresponding TextViews in the item view.
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {


        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
        holder.view.findViewById<TextView>(R.id.itemLibrary_synopsis).text = webtoon.getSynopsis()
        val imageView =holder.view.findViewById<ImageView>(R.id.itemLibrary_image)
        Picasso.get().load(webtoon.getThumbnail()).into(imageView)

        // This line of code is commented out. It seems to be intended for setting an image resource to an ImageView, but it's not completed.
        // holder.view.findViewById<ImageView>(R.id.itemLibrary_image).setImageResource()
    }

    private fun animateSpinner() {
        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 2000
            repeatCount = Animation.INFINITE
        }

        requireView().findViewById<ImageView>(R.id.fragmentLibrary_loading).startAnimation(rotate)
    }

    private fun stopSpinner() {
        requireView().findViewById<ImageView>(R.id.fragmentLibrary_loading).clearAnimation()
        requireView().findViewById<ImageView>(R.id.fragmentLibrary_loading).visibility = View.GONE
    }
}
