package com.eyeshield.expensetracker.networking.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callBack = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(ConnectivityObserver.Status.AVAILABLE)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(ConnectivityObserver.Status.UNAVAILABLE)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(ConnectivityObserver.Status.LOST)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(ConnectivityObserver.Status.LOSING)
                    }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callBack)

            val initialStatus = connectivityManager.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) ?: false
            }

            when (initialStatus) {
                true -> send(ConnectivityObserver.Status.AVAILABLE)
                false -> send(ConnectivityObserver.Status.LOST)
                null -> send(ConnectivityObserver.Status.UNAVAILABLE)
            }

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
    }
}