package com.example.myapplication.network.originals.genreList

data class OriginalMessageGenreList(
    val type: String,
    val service: String,
    val version: String,
    val result: OriginalResultGenreList,
)