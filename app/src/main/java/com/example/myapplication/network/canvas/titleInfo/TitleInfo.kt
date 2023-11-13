package com.example.myapplication.network.canvas.titleInfo

data class TitleInfo(
    val titleNo: Int,
    val language: String,
    val title: String,
    val writingAuthorName: String,
    val representGenre: String,
    val genreInfo: GenreInfo,
    val ageGradeNotice: Boolean,
    val thumbnail: String,
    val starScoreAverage: Float,
    val totalServiceEpisodeCount: Int,
    val readCount: Int,
    val favoriteCount: Int,
    val synopsis: String,
    val linkUrl: String,
    val firstEpisodeNo: Int,
    val lastEpisodeRegisterYmdt: String,
)