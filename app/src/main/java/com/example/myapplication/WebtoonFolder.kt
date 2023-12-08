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

    fun addWebtoon(id: Long){
        val webtoon: Webtoon = Webtoon(id.toInt(),"","","","","","","",0)
        webtoons.add(webtoon)
    }

    // Alex : la surcharge de méthode en kotlin est chelou....
    /*fun addWebtoon(webtoon: Webtoon) {
        webtoons.add(webtoon)
    }*/

    fun removeWebtoon(webtoon: Webtoon) {
        webtoons.remove(webtoon)
    }

    override fun toString(): String {
        val strStart : String = "WebtoonFolder(title='$title', description='$description',"
        val strEnd : String = "',dbid='$dbid)"
        var strMilieu : String = "webtoonsid=["
        for(i in 0..webtoons.size-1){
            strMilieu = strMilieu + webtoons.get(i).getId() + " "
        }
        strMilieu += "]"
        return strStart+strMilieu+strEnd
    }
}