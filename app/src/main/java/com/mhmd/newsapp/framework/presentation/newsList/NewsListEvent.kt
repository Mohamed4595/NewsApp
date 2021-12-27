package com.mhmd.newsapp.framework.presentation.newsList

sealed class NewsListEvent {

    object SearchEvent : NewsListEvent()

    object NextPageEvent : NewsListEvent()
}
