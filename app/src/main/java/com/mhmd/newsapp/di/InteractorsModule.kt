package com.mhmd.newsapp.di

import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.data.network.abstraction.NewsNetworkDatasource
import com.mhmd.newsapp.business.interactors.favoriteList.FavoriteNews
import com.mhmd.newsapp.business.interactors.newsList.CacheNews
import com.mhmd.newsapp.business.interactors.newsList.SearchNews
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchNews(
        newsCacheDatasource: NewsCacheDatasource,
        newsNetworkDatasource: NewsNetworkDatasource,
    ): SearchNews {
        return SearchNews(
            newsCacheDatasource = newsCacheDatasource,
            newsNetworkDatasource = newsNetworkDatasource,
        )
    }

    @ViewModelScoped
    @Provides
    fun provideCacheNews(
        newsCacheDatasource: NewsCacheDatasource,
    ): CacheNews {
        return CacheNews(
            newsCacheDatasource = newsCacheDatasource,
        )
    }

    @ViewModelScoped
    @Provides
    fun provideFavoriteNews(
        newsCacheDatasource: NewsCacheDatasource,
    ): FavoriteNews {
        return FavoriteNews(
            newsCacheDatasource = newsCacheDatasource,
        )
    }
}
