package com.mhmd.newsapp.framework.datasource.network.implementation

import com.mhmd.newsapp.framework.datasource.network.NewsResponse
import com.mhmd.newsapp.framework.datasource.network.abstraction.NewsDtoService
import com.mhmd.newsapp.framework.datasource.network.utils.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class NewsDtoServiceImpl(
    private val client: HttpClient
) : NewsDtoService {

    override suspend fun search(
        pageSize: Int,
        page: Int,
        query: String,
        category: String,
    ): NewsResponse {
        return try {
            client.get {

                url(HttpRoutes.Search)
                // header("Authorization", HttpRoutes.Token)
                header("Content-Type", "application/json; charset=utf-8")

                parameter("country", "us")
                parameter("pageSize", pageSize)
                parameter("page", page)
                parameter("q", query)
                parameter("category", category)
                parameter("apiKey", HttpRoutes.Token)
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.value} ${e.response.status.description}")
            NewsResponse(e.response.status.value.toString(), 0, emptyList())
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.value} ${e.response.status.description}")
            NewsResponse(e.response.status.value.toString(), 0, emptyList())
        } catch (e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.value} ${e.response.status.description}")
            NewsResponse(e.response.status.value.toString(), 0, emptyList())
        } catch (e: Exception) {
            println("Error: ${e.message}")
            NewsResponse("0", 0, emptyList())
        }
    }
}
