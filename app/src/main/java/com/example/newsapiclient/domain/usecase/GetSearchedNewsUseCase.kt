package com.example.newsapiclient.domain.usecase

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(
        keyword: String,
        searchIn: SearchInType,
        sortBy: SearchSortType,
        page: Int
    ): Resource<APIResponse> {
        return newsRepository.getSearchedNews(keyword, searchIn, sortBy, page)
    }
}