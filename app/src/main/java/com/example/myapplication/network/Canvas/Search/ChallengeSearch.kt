package com.example.myapplication.network.Canvas.Search

data class ChallengeSearch (
    val query:String,
    val start:Int,
    val display:Int,
    val total:Int,
    val  titleList: List<com.example.myapplication.network.Canvas.Search.TitleSearch>

)