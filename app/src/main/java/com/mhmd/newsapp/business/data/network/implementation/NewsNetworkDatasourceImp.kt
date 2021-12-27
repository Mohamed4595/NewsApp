package com.mhmd.newsapp.business.data.network.implementation

import com.mhmd.newsapp.business.data.network.abstraction.NewsNetworkDatasource
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.framework.datasource.network.abstraction.NewsDtoService
import com.mhmd.newsapp.framework.datasource.network.mappers.NewsDtoMapper

class NewsNetworkDatasourceImp(
    private val newsDtoService: NewsDtoService,
    private val newsDtoMapper: NewsDtoMapper
) : NewsNetworkDatasource {
    override suspend fun search(
        page: Int,
        query: String,
        category: String,
        pageSize: Int
    ): List<News> {
        return newsDtoMapper.fromEntityList(
            newsDtoService.search(
                category = category,
                page = page,
                pageSize = pageSize,
                query = query
            ).articles
        )
    }
}
