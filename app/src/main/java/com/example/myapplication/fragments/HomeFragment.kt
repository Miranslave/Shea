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
import android.widget.GridLayout
import android.widget.LinearLayout
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
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    val db = Firebase.firestore
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        this.initRecyclerViewDisplay(
            view,
            R.id.fragmentHome_itemsList,
            WebtoonsFoldersListAdapter(this.getMyData(), this, R.layout.item_webtoon_folder),
            GridLayoutManager(context, 2)
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val floatingCreationButton: FloatingActionButton =
            view.findViewById(R.id.fragmentHome_newFolderButton)
        val floatingDeleteButton: FloatingActionButton =
            view.findViewById(R.id.fragmentHome_deleteFolderButton)
        Log.d("SharedPref", uid.toString())
        if (uid != null) {
            Dbgetter(uid)
        }
        //ajout de dossier
        floatingCreationButton.setOnClickListener(fun(_: View) {
            // Create an AlertDialog builder
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Créer un nouveau dossier ?")

            // Set up the input field
            val titleinput = EditText(this.context)
            titleinput.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(titleinput)
            /*
            val descriptioninput = EditText(this.context)
            descriptioninput.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(descriptioninput)
            */
            /*
            val lay = GridLayout()
            lay.orientation = LinearLayout.VERTICAL
            lay.addView(title_input)
            lay.addView(description_input)
            builder.setView(lay)
            */

            // Set up the buttons
            builder.setPositiveButton("Créer") { _, _ ->
                val folderTitle = titleinput.text.toString()
                addFoldertoDb(uid, folderTitle)
                Dbgetter(uid)
            }

            builder.setNegativeButton("Annuler") { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            titleinput.requestFocus()
        })
        //supression  de dossier
        floatingDeleteButton.setOnClickListener(fun(_: View) {
            // setup the alert builder

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Choisit les dossiers à supprimer")
            Firestore().WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
                override fun onSuccess(result: List<Any>) {
                    var arraytitle: Array<String> = emptyArray()
                    var arraydocid: Array<String> = emptyArray()
                    var arrayidtosend: Array<String> = emptyArray()
                    for (doc in result) {
                        var temp: WebtoonFolder = doc as WebtoonFolder
                        arraytitle = arraytitle.plusElement(temp.getTitle())
                        arraydocid = arraydocid.plusElement(temp.getdbid())
                    }
                    builder.setMultiChoiceItems(
                        arraytitle,
                        null
                    ) { dialog, which, isChecked ->
                        // user checked or unchecked a box

                        if (isChecked) {
                            Log.d(
                                "WebtoonFolderDelete",
                                "ajout de titre:" + arraytitle[which] + " dbid:" + arraydocid[which]
                            )
                            arrayidtosend = arrayidtosend.plusElement(arraydocid[which])
                        } else {
                            Log.d(
                                "WebtoonFolderDelete",
                                "suppression de titre:" + arraytitle[which] + " dbid:" + arraydocid[which]
                            )
                            arrayidtosend = rmvelementfromarray(arraydocid, arraydocid[which])
                        }
                    }

                    // add OK and Cancel buttons
                    builder.setPositiveButton("OK") { dialog, which ->
                        // user clicked OK
                        if (!arrayidtosend.isEmpty()) {
                            Log.d("WebtoonFolderDelete", arrayidtosend[0].toString())
                            deleteFolderfromDb(arrayidtosend.toList())
                            Dbgetter(uid)
                        }
                    }
                    builder.setNegativeButton("Cancel", null)

                    // create and show the alert dialog
                    val dialog = builder.create()
                    dialog.show()
                }

                override fun onError(e: Throwable) {

                    Log.d("Error", e.toString())
                    Toast.makeText(context, "Une erreur empêche l'affichage", Toast.LENGTH_SHORT)
                        .show()
                }


            })
        })
    }

    private fun rmvelementfromarray(array: Array<String>, elmttodlt: String): Array<String> {
        var res = emptyArray<String>()
        for (str in array) {
            if (str != elmttodlt) {
                res.plusElement(str)
            }
        }
        return res
    }

    private fun addFoldertoDb(uid: String, title: String) {
        val wbtfolder = hashMapOf(
            "uid" to uid,
            "title" to title,
            "description" to "a link  au + ",
            "webtoonsid" to arrayListOf<Int>()
        )

        db.collection("WebtoonFolder").document()
            .set(wbtfolder)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun deleteFolderfromDb(dbidlist: List<String>) {
        for (id in dbidlist) {
            db.collection("WebtoonFolder").document(id).delete()
        }
    }


    // Mets à jour les folders *
    private fun Dbgetter(uid: String) {
        Firestore().WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<Any>) {
                setRecyclerViewContent(
                    WebtoonsFoldersListAdapter(
                        result,
                        this@HomeFragment,
                        R.layout.item_webtoon_folder
                    )
                )
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
            WebtoonFolder("We chargin :D", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin :V", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin :C", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin (-_-')", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin UwU", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin 404", "Lorem ipsum dolor sit amet", ""),
            WebtoonFolder("We chargin :O", "Lorem ipsum dolor sit amet", "")
        )
    }

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any?) {
        Log.d("info", "Clicked on $position")

        val mainActivity = (activity as? BaseActivity)
        val webtoonFolder = item as WebtoonFolder
        Log.d("Info", "Webtoon Folder current info:" + webtoonFolder.toString())
        mainActivity?.changeFragment(WebtoonFolderDetailsFragment(webtoonFolder))
        mainActivity?.changeTitle(webtoonFolder.getTitle())
    }


    // Set each item folder title to the view
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoonFolder = item as WebtoonFolder
        holder.view.findViewById<TextView>(R.id.itemFolder_title).text = webtoonFolder.getTitle()
    }
}