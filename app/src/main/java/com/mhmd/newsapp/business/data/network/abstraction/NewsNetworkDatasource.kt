package com.mhmd.newsapp.business.data.network.abstraction
import com.mhmd.newsapp.business.domain.model.News

interface NewsNetworkDatasource {

    suspend fun search(
        page: Int,
        query: String,
        category: String,
        pageSize: Int
    ): List<News>
}
