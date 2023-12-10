package com.example.myapplication.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import org.w3c.dom.Text


class CommentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val userEmail: TextView = itemView.findViewById(R.id.itemWebtoonComment_title)
    val commentText: TextView = itemView.findViewById(R.id.itemWebtoonComment_description)
    val time: TextView = itemView.findViewById(R.id.itemWebtoonComment_time)
}