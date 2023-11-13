package com.example.myapplication.network.canvas.search

data class MessageSearch(
    val type: String,
    val service: String,
    val version: String,
    val result: ResultSearch,
)
