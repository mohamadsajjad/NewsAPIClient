package com.example.newsapiclient.data.model.enums

enum class SearchSortType(val type:String) {
    RELEVANCY("relevancy"),
    POPULARITY("popularity"),
    PUBLISHED("publishedAt")
}