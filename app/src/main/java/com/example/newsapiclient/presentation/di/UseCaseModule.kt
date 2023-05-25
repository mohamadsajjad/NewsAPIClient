package com.example.newsapiclient.presentation.di

import com.example.newsapiclient.BuildConfig
import com.example.newsapiclient.data.api.NewsAPIService
import com.example.newsapiclient.domain.repository.NewsRepository
import com.example.newsapiclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsapiclient.domain.usecase.GetSearchedNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetNewsHeadlineUseCase(newsRepository: NewsRepository):GetNewsHeadlinesUseCase{
        return GetNewsHeadlinesUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSearchedNewsUseCase(newsRepository: NewsRepository):GetSearchedNewsUseCase{
        return GetSearchedNewsUseCase(newsRepository)
    }
}