package com.prongbang.connectivity

interface ConnectivityStateListener {
    fun onNetworkStateChange(state: NetworkState)
}