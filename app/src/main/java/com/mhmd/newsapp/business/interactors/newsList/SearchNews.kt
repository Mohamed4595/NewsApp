package com.mhmd.newsapp.business.interactors.newsList

import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.data.network.abstraction.NewsNetworkDatasource
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNews(
    private val newsCacheDatasource: NewsCacheDatasource,
    private val newsNetworkDatasource: NewsNetworkDatasource,
) {

    fun execute(
        page: Int,
        pageSize: Int,
        query: String,
        category: String,
    ): Flow<DataState<List<News>>> = flow {
        try {
            emit(DataState.loading())
            var news = emptyList<News>()
            try {
                news = newsNetworkDatasource.search(
                    pageSize = pageSize,
                    page = page,
                    query = query,
                    category = category
                )
            } catch (e: Exception) {
                // There was a network issue
                e.printStackTrace()
            }

            if (news.isNotEmpty())
                news.forEach {
                    it.isFavorite =
                        newsCacheDatasource.getNews(it.title!!) != null
                }

            emit(DataState.success(news))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }
}
