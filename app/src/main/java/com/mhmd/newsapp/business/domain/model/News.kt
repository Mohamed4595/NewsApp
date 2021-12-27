package com.mhmd.newsapp.business.domain.model

data class News(
    var id: Int?,
    var author: String?,

    var title: String?,

    var description: String?,

    var url: String?,

    var urlToImage: String?,

    var publishedAt: String?,

    var content: String?,

    var isFavorite: Boolean = false,

)
