package com.example.myapplication.network.TitleList

data class TitleInfoSyn (
    val titleNo : Int,
    val language : String,
    val title : String,
    val writingAuthorName:String,
    val representGenre: String,
    val newTitle: Boolean,
    val ageGradeNotice:Boolean,
    val registerYmdt:String,
    val thumbnail: String,
    val starScoreAverage: Float,
    val starScoreCount: Int,
    val readCount: Int,
    val favoriteCount: Int,
    val mana:Int,
    val synopsis: String
)