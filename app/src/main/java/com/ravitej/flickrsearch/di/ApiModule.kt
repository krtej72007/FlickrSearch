package com.ravitej.flickrsearch.di

import com.ravitej.flickrsearch.data.remote.FlickrSearchService
import com.ravitej.flickrsearch.data.repository.FlickrSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideImageSearchService(retrofit: Retrofit): FlickrSearchService =
        retrofit.create(FlickrSearchService::class.java)

    @Singleton
    @Provides
    fun provideImageSearchRepository(imageSearchService: FlickrSearchService) : FlickrSearchRepository {
        return FlickrSearchRepository(imageSearchService)
    }
}