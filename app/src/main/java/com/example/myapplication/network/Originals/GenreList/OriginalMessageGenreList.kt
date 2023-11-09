package com.example.myapplication.network.Originals.GenreList

data class OriginalMessageGenreList (
    val type:String,
    val service:String,
    val version:String,
    val result: OriginalResultGenreList
)