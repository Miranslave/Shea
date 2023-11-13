package com.example.myapplication.network.originals.titleList

data class OriginalTitleList(
    val titles: List<OriginalTitle>,
    val count: Int,
    val now: String,
)