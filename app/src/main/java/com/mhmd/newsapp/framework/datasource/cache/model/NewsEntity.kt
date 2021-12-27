package com.mhmd.newsapp.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(

    // Value from API
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    // Value from API
    @ColumnInfo(name = "author")
    var author: String?,

    // Value from API
    @ColumnInfo(name = "title")
    var title: String?,

    // Value from API
    @ColumnInfo(name = "description")
    var description: String?,

    // Value from API
    @ColumnInfo(name = "url")
    var url: String?,

    // Value from API
    @ColumnInfo(name = "url_to_image")
    var urlToImage: String?,

    // Value from API
    @ColumnInfo(name = "published_at")
    var publishedAt: String?,

    // Value from API
    @ColumnInfo(name = "content")
    var content: String?,

    /**
     * The date this news was "refreshed" in the cache.
     */
    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
)
