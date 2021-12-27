package com.mhmd.newsapp.business.interactors.favoriteList

import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteNews(
    private val newsCacheDatasource: NewsCacheDatasource,
) {

    fun execute(
        page: Int,
        pageSize: Int,
    ): Flow<DataState<List<News>>> = flow {
        try {
            emit(DataState.loading())
            var news = emptyList<News>()
            try {
                news = newsCacheDatasource.getAllNews(
                    pageSize = pageSize,
                    page = page,
                )
            } catch (e: Exception) {
                // There was a network issue
                e.printStackTrace()
            }

            if (news.isNotEmpty())
                news.forEach {
                    it.isFavorite = true
                }

            emit(DataState.success(news))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }
}
