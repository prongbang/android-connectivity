package com.prongbang.connectivity

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

fun NetworkState.hasInternet(): Boolean {
    return (this as? NetworkState.ConnectedState)?.hasInternet == true
}

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
sealed class NetworkState {
    object NotConnectedState : NetworkState()

    sealed class ConnectedState(val hasInternet: Boolean) : NetworkState() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        data class Connected(val capabilities: NetworkCapabilities) : ConnectedState(
            capabilities.hasCapability(NET_CAPABILITY_INTERNET)
        )

        @Suppress("DEPRECATION")
        data class ConnectedLegacy(val networkInfo: NetworkInfo) : ConnectedState(
            networkInfo.isConnectedOrConnecting
        )
    }
}