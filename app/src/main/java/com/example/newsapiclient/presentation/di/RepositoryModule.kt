package com.example.newsapiclient.presentation.di

import com.example.newsapiclient.data.api.NewsAPIService
import com.example.newsapiclient.data.repository.NewsRepositoryImpl
import com.example.newsapiclient.data.repository.dataSource.NewsRemoteDataSource
import com.example.newsapiclient.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.example.newsapiclient.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(newsRemoteDataSource: NewsRemoteDataSource):NewsRepository{
        return NewsRepositoryImpl(newsRemoteDataSource)
    }
}