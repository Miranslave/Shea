package com.example.myapplication.fragments

// Import necessary Android and project-specific classes
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.Image.ImageLoader
import com.example.myapplication.R
import com.example.myapplication.Webtoon
import com.example.myapplication.activities.BaseActivity

// Define a Fragment to show the details of a Webtoon
class WebtoonDetailsFragment(private val webtoon: Webtoon) : Fragment() {

    // Inflate the layout for this fragment
    private lateinit var imageLoader: ImageLoader
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        imageLoader= ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())
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
        imageLoader.load(imgview,webtoon.getThumbnail())
        // Set the onClickListener for the URL button
        view.findViewById<ImageButton>(R.id.fragmentWebtoonDetails_urlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(webtoon.getLinkUrl())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
    }
}
