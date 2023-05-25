package com.example.newsapiclient.data.repository.dataSourceImpl

import com.example.newsapiclient.data.api.NewsAPIService
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import com.example.newsapiclient.data.repository.dataSource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService
) : NewsRemoteDataSource {

    override suspend fun getTopHeadlines(
        country: String,
        category: String,
        page: Int
    ): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(
            country,
            category,
            page
        )
    }

    override suspend fun getSearchedNews(
        keyword: String,
        searchIn: SearchInType,
        sortBy: SearchSortType,
        page: Int
    ): Response<APIResponse> {
        return newsAPIService.getSearchedNews(
            keyword,
            searchIn,
            sortBy,
            page
        )
    }
}