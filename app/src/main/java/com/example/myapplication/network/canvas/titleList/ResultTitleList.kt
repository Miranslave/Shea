package com.example.myapplication.network.canvas.titleList

data class ResultTitleList(
    val startIndex: Int,
    val totalCount: Int,
    val titleList: TitleList,
    val exposureGenre: Boolean,
)