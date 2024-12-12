package com.eyeshield.expensetracker.networking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClientModule(httpClientFactory: HttpClientFactory): HttpClient {
        return httpClientFactory.build()
    }
}