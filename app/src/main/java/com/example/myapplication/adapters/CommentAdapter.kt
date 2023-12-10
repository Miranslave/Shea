package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.firestoredb.data.Comment

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
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}