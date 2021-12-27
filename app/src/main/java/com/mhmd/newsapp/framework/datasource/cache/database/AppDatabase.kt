package com.mhmd.newsapp.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhmd.newsapp.framework.datasource.cache.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        const val DATABASE_NAME: String = "news_db"
    }
}
