package com.mhmd.newsapp.business.data.cache.implementation

import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.framework.datasource.cache.abstraction.NewsDaoService

class NewsCacheDatasourceImp(private val newsDaoService: NewsDaoService) : NewsCacheDatasource {
    override suspend fun insertNews(news: News): Long {
        return newsDaoService.insertNews(news)
    }

    override suspend fun deleteAllNews() {
        return newsDaoService.deleteAllNews()
    }

    override suspend fun deleteNew(primaryKey: Int): Int {
        return newsDaoService.deleteNew(primaryKey)
    }

    override suspend fun getAllNews(page: Int, pageSize: Int): List<News> {
        return newsDaoService.getAllNews(page, pageSize)
    }

    override suspend fun getNews(title: String): News? {
        return newsDaoService.getNews(title)
    }
}
