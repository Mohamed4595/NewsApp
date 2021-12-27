package com.mhmd.newsapp.framework.datasource.network

import com.mhmd.newsapp.framework.datasource.network.model.NewsDto
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    var status: String,
    var totalResults: Int,
    var articles: List<NewsDto>,
)
