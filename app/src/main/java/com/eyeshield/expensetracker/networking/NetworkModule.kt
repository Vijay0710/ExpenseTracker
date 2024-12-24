package com.eyeshield.expensetracker.networking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesHttpClient(httpClientFactory: HttpClientFactory): HttpClient {
        return httpClientFactory.build()
    }

    @Provides
    @Singleton
    fun provideApiCallScope(): CoroutineScope {
        // Providing SupervisorJob so the coroutines fail independently
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}