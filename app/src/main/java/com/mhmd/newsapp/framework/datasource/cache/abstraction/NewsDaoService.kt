package com.mhmd.newsapp.framework.datasource.cache.abstraction

import com.mhmd.newsapp.business.domain.model.News

interface NewsDaoService {

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
