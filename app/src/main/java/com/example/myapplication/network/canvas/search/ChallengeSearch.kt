package com.example.myapplication.network.canvas.search

data class ChallengeSearch(
    val query: String,
    val start: Int,
    val display: Int,
    val total: Int,
    val titleList: List<TitleSearch>,

    )