package com.example.myapplication

class WebtoonFolder {
    private val title: String
    private val description: String
    private val webtoons: ArrayList<Webtoon>
    private val dbid: String


    constructor(title: String, description: String,dbid: String) {
        this.title = title
        this.description = description
        this.webtoons = ArrayList()
        this.dbid = dbid
    }

    fun getTitle(): String {
        return title
    }
    fun getdbid():String{
        return dbid
    }

    fun getDescription(): String {
        return description
    }

    fun getWebtoons(): ArrayList<Webtoon> {
        return webtoons
    }

    fun addWebtoon(webtoon: Webtoon) {
        webtoons.add(webtoon)
    }

    fun removeWebtoon(webtoon: Webtoon) {
        webtoons.remove(webtoon)
    }

    override fun toString(): String {
        return "WebtoonFolder(title='$title', description='$description','dbid='$dbid)"
    }
}