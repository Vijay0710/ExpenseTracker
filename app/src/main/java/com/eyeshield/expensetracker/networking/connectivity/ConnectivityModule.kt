package com.eyeshield.expensetracker.networking.connectivity

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ConnectivityModule {

    @Binds
    @Singleton
    fun bindsConnectivityObserver(connectivityObserver: NetworkConnectivityObserver): ConnectivityObserver
}