package com.mhmd.newsapp.framework.datasource.cache.mappers

import com.mhmd.newsapp.business.data.util.ModelMapper
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.framework.datasource.cache.model.NewsEntity
import com.mhmd.newsapp.utils.DateUtils

class NewsEntityMapper : ModelMapper<NewsEntity, News> {

    override fun mapToModel(model: NewsEntity): News {
        return News(
            id = model.id,
            title = model.title,
            description = model.description,
            author = model.author,
            publishedAt = model.publishedAt,
            urlToImage = model.urlToImage,
            url = model.url,
            content = model.content
        )
    }

    override fun mapFromModel(model: News): NewsEntity {
        return NewsEntity(
            id = null,
            title = model.title,
            description = model.description,
            author = model.author,
            publishedAt = model.publishedAt,
            urlToImage = model.urlToImage,
            url = model.url,
            content = model.content,
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())

        )
    }

    fun fromEntityList(initial: List<NewsEntity>): List<News> {
        return initial.map { mapToModel(it) }
    }

    fun toEntityList(initial: List<News>): List<NewsEntity> {
        return initial.map { mapFromModel(it) }
    }
}
