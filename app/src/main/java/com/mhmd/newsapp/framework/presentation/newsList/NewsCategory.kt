package com.mhmd.newsapp.framework.presentation.newsList

enum class NewsCategory(val value: String) {

    General("General"),
    Business("Business"),
    Entertainment("Entertainment"),
    Health("Health"),
    Science("Science"),
    Sports("Sports"),
    Technology("Technology"),
}

fun getAllNewsCategories(): List<NewsCategory> {
    return listOf(
        NewsCategory.General, NewsCategory.Business,
        NewsCategory.Entertainment,
        NewsCategory.Health, NewsCategory.Science, NewsCategory.Sports,
        NewsCategory.Technology
    )
}

fun getNewsCategory(value: String): NewsCategory? {
    val map = NewsCategory.values().associateBy(NewsCategory::value)
    return map[value]
}
