package com.mhmd.newsapp.framework.presentation.newsList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.business.interactors.newsList.CacheNews
import com.mhmd.newsapp.business.interactors.newsList.SearchNews
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
class NewsListViewModel
@Inject
constructor(
    private val searchNews: SearchNews,
    private val cacheNews: CacheNews,
) : ViewModel() {

    val news: MutableState<List<News>> = mutableStateOf(ArrayList())

    val selectedNewsItem: MutableState<News?> = mutableStateOf(null)

    val query = mutableStateOf("")

    val selectedCategory: MutableState<NewsCategory?> =
        mutableStateOf(NewsCategory.General)

    val refreshNews: MutableState<Boolean> = mutableStateOf(false)

    val loading = mutableStateOf(false)

    // Pagination starts at '1' (-1 = exhausted)
    val page = mutableStateOf(1)

    private var newsListScrollPosition: Int = 0

    var categoryScrollPosition: Int = 0

    val dialogQueue = DialogQueue()

    init {
        onTriggerEvent(SearchEvent)
    }

    fun onTriggerEvent(event: NewsListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SearchEvent -> {
                        search()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun search() {

        news.value = listOf()
        page.value = 1
        searchNews.execute(
            page = page.value,
            query = query.value,
            pageSize = Constants.NEWS_PAGINATION_PAGE_SIZE,
            category = if (selectedCategory.value == null) ""
            else selectedCategory.value!!.value.lowercase()
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                news.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {

        // prevent duplicate event due to recompose happening to quickly
        if ((newsListScrollPosition + 1) >= (page.value * Constants.NEWS_PAGINATION_PAGE_SIZE)) {
            incrementPage()

            if (page.value > 1) {
                searchNews.execute(
                    page = page.value,
                    query = query.value,
                    pageSize = Constants.NEWS_PAGINATION_PAGE_SIZE,
                    category = if (selectedCategory.value == null) "" else selectedCategory.value!!.value.lowercase()
                ).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                        appendNews(list)
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
    private fun appendNews(news: List<News>) {
        val current = ArrayList(this.news.value)
        current.addAll(news)
        this.news.value = current
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeNewsScrollPosition(position: Int) {
        setListScrollPosition(position)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getNewsCategory(category)
        if (newCategory != null || newCategory != NewsCategory.General)
            if (newCategory == selectedCategory.value) {
                selectedCategory.value = NewsCategory.General
                categoryScrollPosition = 0
            } else
                selectedCategory.value = newCategory
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    fun onFavorite(index: Int) {

        news.value[index].isFavorite = !news.value[index].isFavorite

        cacheNews.execute(news.value[index]).onEach { dataState ->

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)

        refreshNews.value = !refreshNews.value
    }

    private fun setListScrollPosition(position: Int) {
        newsListScrollPosition = position
    }

    private fun setPage(page: Int) {
        this.page.value = page
    }

    private fun setQuery(query: String) {
        this.query.value = query
    }

    fun onRefresh(newsTitle: String) {
        var newsIndex: Int = -1
        for (element in news.value) {
            if (element.title == newsTitle) {
                newsIndex =
                    news.value.indexOf(element)
                break
            }
        }
        if (newsIndex != -1) {
            news.value[newsIndex].isFavorite =
                !news.value[newsIndex].isFavorite
        } else {
            if (selectedNewsItem.value != null) {
                selectedNewsItem.value!!.isFavorite = !selectedNewsItem.value!!.isFavorite
                setSelectedNewsItem(selectedNewsItem.value!!)
            }
        }

        refreshNews.value =
            !refreshNews.value
    }

    fun setSelectedNewsItem(newsItem: News?) {
        selectedNewsItem.value = newsItem
    }
}
