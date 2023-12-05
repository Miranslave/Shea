package com.example.myapplication.fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.WebtoonFolder
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsFoldersListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class HomeFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    val db = Firebase.firestore
    val sharedPref= activity?.getPreferences(Context.MODE_PRIVATE)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)



        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        this.initRecyclerViewDisplay(view, R.id.fragmentHome_itemsList, WebtoonsFoldersListAdapter(this.getMyData(), this, R.layout.item_webtoon_folder), GridLayoutManager(context, 2))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val floatingCreationButton: FloatingActionButton = view.findViewById(R.id.fragmentHome_newFolderButton)
        Log.d("SharedPref", uid.toString())
        if (uid != null) {
            Dbgetter(uid)
        }

        floatingCreationButton.setOnClickListener(fun(_: View) {
            // Create an AlertDialog builder
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Quel est le nom du dossier ?")

            // Set up the input field
            val input = EditText(this.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("Créer") { _, _ ->
                val folderTitle = input.text.toString()
                addFoldertoDb(uid,folderTitle)
                Dbgetter(uid)
            }

            builder.setNegativeButton("Annuler") { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            input.requestFocus()
        })
    }

    fun addFoldertoDb(uid: String,title:String){
        val wbtfolder = hashMapOf(
            "uid" to uid,
            "title" to title,
            "description" to "a link  au + ",
        )

        db.collection("WebtoonFolder").document()
            .set(wbtfolder)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }


    private fun Dbgetter(uid:String){
        val res: ArrayList<Any> = ArrayList()
        Firestore().WebtoonFolder(uid,object : FirestoreCallback<List<WebtoonFolder>>{
            override fun onSuccess(result: List<Any>) {

                setRecyclerViewContent(WebtoonsFoldersListAdapter(result,this@HomeFragment,R.layout.item_webtoon_folder))
            }
            override fun onError(e: Throwable) {

                Log.d("Error", e.toString())
                Toast.makeText(context, "Une erreur empêche l'affichage", Toast.LENGTH_SHORT).show()
            }
        })
    }
    // Give folders name to view
    private fun getMyData(): List<Any> {

        return listOf(
            WebtoonFolder("Action", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Aventure", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Comédie", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Drame", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Fantastique", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Horreur", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Romance", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl.")
        )
    }

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any?) {
        Log.d("info", "Clicked on $position")

        val mainActivity = (activity as? BaseActivity)
        val webtoonFolder = item as WebtoonFolder

        mainActivity?.changeFragment(WebtoonFolderDetailsFragment(webtoonFolder))
        mainActivity?.changeTitle(webtoonFolder.getTitle())
    }

    // Set each item folder title to the view
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoonFolder = item as WebtoonFolder
        holder.view.findViewById<TextView>(R.id.itemFolder_title).text = webtoonFolder.getTitle()
    }
}