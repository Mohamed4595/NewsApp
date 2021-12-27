package com.mhmd.newsapp.framework.datasource.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(

    var author: String?,

    var title: String?,

    var description: String?,

    var url: String?,

    var urlToImage: String?,

    var publishedAt: String?,

    var content: String?,

)
