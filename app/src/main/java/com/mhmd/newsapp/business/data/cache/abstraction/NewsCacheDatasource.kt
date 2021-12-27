package com.mhmd.newsapp.business.data.cache.abstraction

import com.mhmd.newsapp.business.domain.model.News

interface NewsCacheDatasource {

    suspend fun insertNews(news: News): Long

    suspend fun deleteAllNews()

    suspend fun deleteNew(primaryKey: Int): Int

    suspend fun getAllNews(
        page: Int,
        pageSize: Int
    ): List<News>

    suspend fun getNews(
        title: String,
    ): News?
}
