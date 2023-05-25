package com.example.newsapiclient.data.repository.dataSource

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import retrofit2.Response

interface NewsRemoteDataSource {

    suspend fun getTopHeadlines(
        country: String,
        category: String,
        page: Int
    ): Response<APIResponse>

    suspend fun getSearchedNews(
        keyword: String,
        searchIn: SearchInType,
        sortBy: SearchSortType,
        page: Int
    ):Response<APIResponse>
}