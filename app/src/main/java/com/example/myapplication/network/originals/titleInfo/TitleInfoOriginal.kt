package com.example.myapplication.network.originals.titleInfo

data class TitleInfoOriginal(
    val titleNo: Int,
    val language: String,
    val writingAuthorName: String,
    val title: String,
    val representGenre: String,
    val synopsis: String,
    val restTerminationStatus: String,
    val newTitle: Boolean,
    val ageGradeNotice: Boolean,
    val totalServiceEpisodeCount: Int,
    val lastEpisodeRegisterYmdt: String,
    val thumbnail: String,
    val linkUrl: String,
    val starScoreAverage: Float,

    val readCount: Int,
    val favoriteCount: Int,

    )