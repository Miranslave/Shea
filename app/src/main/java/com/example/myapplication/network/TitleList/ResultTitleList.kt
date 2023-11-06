package com.example.myapplication.network.TitleList

import com.example.myapplication.network.Genrelist.ResultGenrelist

data class ResultTitleList (
    val startIndex: Int,
    val totalCount:Int,
    val titleList: TitleList,
    val exposureGenre: Boolean
)