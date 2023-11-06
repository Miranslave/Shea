package com.example.myapplication.network.Home

import com.example.myapplication.network.DataClass.Global.Genre
import com.example.myapplication.network.DataClass.Global.Titleinfo

data class ResultHome(

    val votePeriodForRisingStar: Boolean,
    val weeklyHotTitleList: List<Titleinfo>,
    val popularByGenreList: List<Genre>,
    val weeklyHotByGenreList: List<Genre>

)
