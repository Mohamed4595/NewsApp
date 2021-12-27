package com.mhmd.newsapp.framework.presentation.favoriteList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.business.interactors.favoriteList.FavoriteNews
import com.mhmd.newsapp.business.interactors.newsList.CacheNews
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent.NextPageEvent
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent.SearchEvent
import com.mhmd.newsapp.framework.presentation.utils.DialogQueue
import com.mhmd.newsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel
@Inject
constructor(
    private val favoriteNews: FavoriteNews,
    private val cacheNews: CacheNews,
) : ViewModel() {

    val newsFavorite: MutableState<List<News>> = mutableStateOf(ArrayList())

    val dialogQueue = DialogQueue()
    val loading = mutableStateOf(false)

    // Pagination starts at '1' (-1 = exhausted)
    val pageFavorite = mutableStateOf(1)
    val selectedNewsItem: MutableState<News?> = mutableStateOf(null)

    private var newsListScrollPositionFavorite: Int = 0

    fun onTriggerEventFavorite(event: NewsListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SearchEvent -> {
                        getNewsFromCache()
                    }
                    is NextPageEvent -> {
                        nextPageFromCache()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getNewsFromCache() {

        newsFavorite.value = listOf()
        pageFavorite.value = 1
        favoriteNews.execute(
            page = pageFavorite.value,
            pageSize = Constants.NEWS_PAGINATION_PAGE_SIZE,
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                newsFavorite.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPageFromCache() {

        // prevent duplicate event due to recompose happening to quickly
        if ((newsListScrollPositionFavorite + 1) >= (pageFavorite.value * Constants.NEWS_PAGINATION_PAGE_SIZE)) {
            incrementPageFavorite()

            if (pageFavorite.value > 1) {
                favoriteNews.execute(
                    page = pageFavorite.value,
                    pageSize = Constants.NEWS_PAGINATION_PAGE_SIZE,
                ).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                        appendNewsFavorite(list)
                    }

                    dataState.error?.let { error ->
                        dialogQueue.appendErrorMessage("An Error Occurred", error)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    /**
     * Append new news to the current list of news
     */
    private fun appendNewsFavorite(news: List<News>) {
        val current = ArrayList(newsFavorite.value)
        current.addAll(news)
        newsFavorite.value = current
    }

    private fun incrementPageFavorite() {
        setPage(pageFavorite.value + 1)
    }

    fun onChangeNewsScrollPositionFavorite(position: Int) {
        setListScrollPosition(position)
    }

    fun onFavorite(newsItem: News) {

        cacheNews.execute(newsItem).onEach { dataState ->
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)

        val current: ArrayList<News> = ArrayList()

        if (newsItem.isFavorite) {
            for (element in newsFavorite.value) {
                if (element.title != newsItem.title) {
                    current.add(element = element)
                } else {
                    if (selectedNewsItem.value != null) {
                        element.isFavorite = false
                        setSelectedNewsItem(element)
                    }
                }
            }
        } else {
            current.addAll(newsFavorite.value)
            current.add(newsItem)
            if (selectedNewsItem.value != null) {
                newsItem.isFavorite = true
                setSelectedNewsItem(newsItem)
            }
        }

        newsFavorite.value = current
    }

    private fun setListScrollPosition(position: Int) {
        newsListScrollPositionFavorite = position
    }

    private fun setPage(page: Int) {
        this.pageFavorite.value = page
    }

    fun setSelectedNewsItem(newsItem: News?) {
        selectedNewsItem.value = newsItem
    }
}
