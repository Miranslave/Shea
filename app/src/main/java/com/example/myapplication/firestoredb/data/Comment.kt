package com.example.myapplication.firestoredb.data

import com.google.firebase.Timestamp

data class Comment(
    val userEmail: String,
    val commentText: String,
    val time: Timestamp
)
