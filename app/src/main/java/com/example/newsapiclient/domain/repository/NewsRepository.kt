package com.example.newsapiclient.domain.repository

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import com.example.newsapiclient.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNewsHeadlines(
        country: String,
        category: String,
        page: Int
    ): Resource<APIResponse>

    suspend fun getSearchedNews(
        keyword: String,
        searchIn: SearchInType,
        sortBy: SearchSortType,
        page: Int
    ): Resource<APIResponse>

    suspend fun saveNews(article: Article)
    suspend fun deleteNews(article: Article)
    fun getSavedNews(): Flow<List<Article>>
}