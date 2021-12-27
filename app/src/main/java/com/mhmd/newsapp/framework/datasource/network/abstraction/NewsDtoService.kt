package com.mhmd.newsapp.framework.datasource.network.abstraction

import com.mhmd.newsapp.framework.datasource.network.NewsResponse

interface NewsDtoService {

    suspend fun search(
        pageSize: Int,
        page: Int,
        query: String,
        category: String,
    ): NewsResponse
}
