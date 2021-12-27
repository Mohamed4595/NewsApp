package com.mhmd.newsapp.di

import com.mhmd.newsapp.business.data.network.abstraction.NewsNetworkDatasource
import com.mhmd.newsapp.business.data.network.implementation.NewsNetworkDatasourceImp
import com.mhmd.newsapp.framework.datasource.network.abstraction.NewsDtoService
import com.mhmd.newsapp.framework.datasource.network.implementation.NewsDtoServiceImpl
import com.mhmd.newsapp.framework.datasource.network.mappers.NewsDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDtoNewsMapper(): NewsDtoMapper {
        return NewsDtoMapper()
    }

    @Singleton
    @Provides
    fun provideNewsDatasource(
        newsDtoService: NewsDtoService,
        newsDtoMapper: NewsDtoMapper
    ): NewsNetworkDatasource {
        return NewsNetworkDatasourceImp(newsDtoService, newsDtoMapper)
    }

    @Singleton
    @Provides
    fun provideNewsService(): NewsDtoService {
        return NewsDtoServiceImpl(
            client = HttpClient(Android) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                install(JsonFeature) {

                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        }
                    )
                }
            }
        )
    }
}
