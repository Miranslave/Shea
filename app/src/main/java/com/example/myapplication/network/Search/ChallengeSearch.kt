package com.example.myapplication.network.Search

import android.icu.text.CaseMap.Title
import com.example.myapplication.network.DataClass.Global.Titleinfo

data class ChallengeSearch (
    val query:String,
    val start:Int,
    val display:Int,
    val total:Int,
    val  titleList: List<TitleSearch>

)