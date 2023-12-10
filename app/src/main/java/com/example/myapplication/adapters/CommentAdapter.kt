package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.currentRecomposeScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.firestoredb.data.Comment
import java.util.Calendar

class CommentAdapter (private val commentList : List<Comment>) : RecyclerView.Adapter<CommentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_webtoon_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.userEmail.text = currentComment.userEmail
        holder.commentText.text = currentComment.commentText

        val date = currentComment.time.toDate()
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        holder.time.text = calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                (calendar.get(Calendar.MONTH) + 1).toString() + "/" +
                calendar.get(Calendar.YEAR).toString()
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}