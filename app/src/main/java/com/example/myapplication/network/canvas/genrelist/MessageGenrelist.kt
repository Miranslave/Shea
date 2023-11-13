package com.example.myapplication.network.canvas.genrelist


data class MessageGenrelist(
    val type: String,
    val service: String,
    val version: String,
    val result: ResultGenrelist,
)