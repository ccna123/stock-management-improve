package com.example.sol_denka_stockmanagement.helper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NetworkConnectionObserver @Inject constructor(
    context: Context
) {
    private val connectManager = context.getSystemService<ConnectivityManager>()!!
    val isConnected: Flow<Boolean>
        get() = callbackFlow {
            val initialConnected = isNetworkAvailable()
            trySend(initialConnected)

            val callback = object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(isNetworkAvailable())
                }

                override fun onUnavailable() {
                    trySend(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                    trySend(connected)
                }
            }
            connectManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectManager.unregisterNetworkCallback(callback)
            }
        }
    private fun isNetworkAvailable(): Boolean {
        val network = connectManager.activeNetwork ?: return false
        val capabilities = connectManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}