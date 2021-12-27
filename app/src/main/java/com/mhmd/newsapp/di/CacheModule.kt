package com.mhmd.newsapp.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.mhmd.newsapp.business.data.cache.abstraction.NewsCacheDatasource
import com.mhmd.newsapp.business.data.cache.implementation.NewsCacheDatasourceImp
import com.mhmd.newsapp.framework.datasource.cache.abstraction.NewsDaoService
import com.mhmd.newsapp.framework.datasource.cache.database.AppDatabase
import com.mhmd.newsapp.framework.datasource.cache.database.NewsDao
import com.mhmd.newsapp.framework.datasource.cache.implementation.NewsDaoServiceImp
import com.mhmd.newsapp.framework.datasource.cache.mappers.NewsEntityMapper
import com.mhmd.newsapp.framework.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .setQueryCallback(
                RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
                    println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                },
                Executors.newSingleThreadExecutor()
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(db: AppDatabase): NewsDao {
        return db.newsDao()
    }

    @Singleton
    @Provides
    fun provideNewsDaoService(newsDao: NewsDao, newsEntityMapper: NewsEntityMapper): NewsDaoService {
        return NewsDaoServiceImp(newsDao, newsEntityMapper)
    }
    @Singleton
    @Provides
    fun provideNewsDatasource(newsDaoService: NewsDaoService): NewsCacheDatasource {
        return NewsCacheDatasourceImp(newsDaoService)
    }
    @Singleton
    @Provides
    fun provideEntityNewsMapper(): NewsEntityMapper {
        return NewsEntityMapper()
    }
}
