package com.example.myapplication.models

class WebtoonFolder {
    private val title: String
    private val description: String
    private val webtoons: ArrayList<Webtoon>
    private val databaseId: String
    private val allowDeletion: Boolean


    constructor(title: String, description: String, databaseId: String, allowDeletion: Boolean = true) {
        this.title = title
        this.description = description
        this.webtoons = ArrayList()
        this.databaseId = databaseId
        this.allowDeletion = allowDeletion
    }

    fun getTitle(): String {
        return title
    }

    fun getDatabaseId(): String {
        return databaseId
    }

    fun getDescription(): String {
        return description
    }

    fun getWebtoons(): ArrayList<Webtoon> {
        return webtoons
    }

    fun addWebtoon(id: Long) {
        val webtoon: Webtoon = Webtoon(id.toInt(), "", "", "", "", "", "", "", 0)
        webtoons.add(webtoon)
    }

    fun removeWebtoon(webtoon: Webtoon) {
        webtoons.remove(webtoon)
    }

    override fun toString(): String {
        val webtoonsId = webtoons.joinToString(" ") { it.getId().toString() }
        return "WebtoonFolder(title='$title', description='$description', webtoonsid=[$webtoonsId], dbid='$databaseId)"
    }

    fun canBeDeleted(): Boolean {
        return this.allowDeletion
    }
}