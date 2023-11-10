package com.example.myapplication.network.Canvas.TitleList

import com.example.myapplication.network.Canvas.Genrelist.ResultGenrelist

data class ResultTitleList (
    val startIndex: Int,
    val totalCount:Int,
    val titleList: com.example.myapplication.network.Canvas.TitleList.TitleList,
    val exposureGenre: Boolean
)