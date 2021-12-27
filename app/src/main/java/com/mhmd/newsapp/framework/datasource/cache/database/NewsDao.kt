package com.mhmd.newsapp.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mhmd.newsapp.framework.datasource.cache.model.NewsEntity

@Dao
interface NewsDao {

    @Insert
    suspend fun insertNews(news: NewsEntity): Long

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Query("DELETE FROM news WHERE id = :primaryKey")
    suspend fun deleteNew(primaryKey: Int): Int

    @Query("SELECT * FROM news WHERE title LIKE '%' || :title || '%'")
    suspend fun getNews(title: String): NewsEntity?

    /**
     * Retrieve News for a particular page.
     * Ex: page = 2 retrieves News from 30-60.
     * Ex: page = 3 retrieves News from 60-90
     */
    @Query(
        """
        SELECT * FROM news 
        ORDER BY published_at DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """
    )
    suspend fun getAllNews(
        page: Int,
        pageSize: Int
    ): List<NewsEntity>
}
