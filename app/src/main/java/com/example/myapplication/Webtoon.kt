package com.example.myapplication

// Class representing a Webtoon
class Webtoon {
    // Private variables representing the properties of a Webtoon
    private val id: Int
    private val author: String
    private val title: String
    private val synopsis: String
    private val genre: String
    private val linkUrl: String
    private val thumbnail: String
    private val restTerminationStatus: String
    private val totalEpisodeCount: Int

    // Constructor for the Webtoon class
    constructor(id: Int, title: String, synopsis: String, author: String, genre: String, restTerminationStatus: String, thumbnail: String, linkUrl: String, totalEpisodeCount: Int) {
        this.id = id
        this.title = title
        this.synopsis = synopsis
        this.author = author
        this.genre = genre
        this.restTerminationStatus = restTerminationStatus
        this.thumbnail = thumbnail
        this.linkUrl = linkUrl
        this.totalEpisodeCount = totalEpisodeCount
    }

    // Getter methods for the properties of a Webtoon
    fun getId(): Int {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun getSynopsis(): String {
        return synopsis
    }

    fun getAuthor(): String {
        return author
    }

    fun getGenre(): String {
        return genre
    }

    fun getRestTerminationStatus(): String {
        return restTerminationStatus
    }

    fun getThumbnail(): String {
        return thumbnail
    }

    fun getLinkUrl(): String {
        return linkUrl
    }

    fun getTotalEpisodeCount(): Int {
        return totalEpisodeCount
    }

    // Method to return a string representation of a Webtoon
    override fun toString(): String {
        return "Webtoon(id=$id, title='$title', synopsis='$synopsis', author='$author', genre='$genre', restTerminationStatus='$restTerminationStatus', thumbnail='$thumbnail', linkUrl='$linkUrl')"
    }
}
