package com.example.newsapiclient.data.api


import com.example.newsapiclient.BuildConfig
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String,
        @Query("category")
        category: String,
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<APIResponse>

    @GET("everything")
    suspend fun getSearchedNews(
        @Query("q")
        keyword:String,
        @Query("searchIn")
        searchIn:SearchInType,
        @Query("sortBy")
        sortBy:SearchSortType,
        @Query("page")
        page: Int,
        @Query("pageSize")
        pageSize:Int = 20,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ):Response<APIResponse>
}