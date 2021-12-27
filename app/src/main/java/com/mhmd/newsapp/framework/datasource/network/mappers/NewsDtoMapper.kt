package com.mhmd.newsapp.framework.datasource.network.mappers

import com.mhmd.newsapp.business.data.util.ModelMapper
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.framework.datasource.network.model.NewsDto

class NewsDtoMapper : ModelMapper<NewsDto, News> {

    override fun mapToModel(model: NewsDto): News {
        return News(
            id = 0,
            title = model.title,
            description = model.description,
            author = model.author,
            content = model.content,
            publishedAt = model.publishedAt,
            url = model.url,
            urlToImage = model.urlToImage,
        )
    }

    override fun mapFromModel(model: News): NewsDto {
        return NewsDto(
            title = model.title,
            description = model.description,
            author = model.author,
            content = model.content,
            publishedAt = model.publishedAt,
            url = model.url,
            urlToImage = model.urlToImage,
        )
    }

    fun fromEntityList(initial: List<NewsDto>): List<News> {
        return initial.map { mapToModel(it) }
    }

    fun toEntityList(initial: List<News>): List<NewsDto> {
        return initial.map { mapFromModel(it) }
    }
}
