package com.mhmd.newsapp.business.interactors.newsList

import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CacheNews(
    private val newsCacheDatasource: NewsCacheDatasource,
) {

    fun execute(
        newsItem: News
    ): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())
            val cacheNewsItem: News? = newsCacheDatasource.getNews(newsItem.title!!)
            if (cacheNewsItem == null) {
                newsCacheDatasource.insertNews(news = newsItem)
            } else {
                newsCacheDatasource.deleteNew(primaryKey = cacheNewsItem.id!!)
            }

            emit(DataState.success(true))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }
}
