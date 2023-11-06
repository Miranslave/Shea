package com.example.myapplication.network.Search

data class MessageSearch (
    val type: String,
    val service:String,
    val version: String,
    val result: ResultSearch
)
