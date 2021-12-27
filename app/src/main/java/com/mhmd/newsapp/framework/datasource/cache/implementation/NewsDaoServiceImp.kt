package com.mhmd.newsapp.framework.datasource.cache.implementation

import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.framework.datasource.cache.abstraction.NewsDaoService
import com.mhmd.newsapp.framework.datasource.cache.database.NewsDao
import com.mhmd.newsapp.framework.datasource.cache.mappers.NewsEntityMapper

class NewsDaoServiceImp(
    private val newsDao: NewsDao,
    private val newsEntityMapper: NewsEntityMapper
) :
    NewsDaoService {
    override suspend fun insertNews(news: News): Long {
        return newsDao.insertNews(newsEntityMapper.mapFromModel(news))
    }

    override suspend fun deleteAllNews() {
        return newsDao.deleteAllNews()
    }

    override suspend fun deleteNew(primaryKey: Int): Int {
        return newsDao.deleteNew(primaryKey)
    }

    override suspend fun getAllNews(page: Int, pageSize: Int): List<News> {
        return newsEntityMapper.fromEntityList(newsDao.getAllNews(page, pageSize))
    }

    override suspend fun getNews(title: String): News? {
        return if (newsDao.getNews(title) == null) null else newsEntityMapper.mapToModel(newsDao.getNews(title)!!)
    }
}
