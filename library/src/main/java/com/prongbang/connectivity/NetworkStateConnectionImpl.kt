package com.prongbang.connectivity

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

@Suppress("DEPRECATION")
class NetworkStateConnectionImpl(
    private val cm: ConnectivityManager
) : NetworkStateConnection {

    override fun getNetworkState(intent: Intent): NetworkState {
        val networkInfo = cm.activeNetworkInfo
        val fallbackNetworkInfo: NetworkInfo? =
            intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO)
        return if (networkInfo?.isConnectedOrConnecting == true) {
            NetworkState.ConnectedState.ConnectedLegacy(networkInfo)
        } else if (networkInfo != null && fallbackNetworkInfo != null &&
            networkInfo.isConnectedOrConnecting != fallbackNetworkInfo.isConnectedOrConnecting
        ) {
            NetworkState.ConnectedState.ConnectedLegacy(fallbackNetworkInfo)
        } else {
            val state = networkInfo ?: fallbackNetworkInfo
            if (state != null) NetworkState.ConnectedState.ConnectedLegacy(state) else NetworkState.NotConnectedState
        }
    }
}