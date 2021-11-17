package com.prongbang.connectivity

import android.content.Intent

interface NetworkStateConnection {
    fun getNetworkState(intent: Intent): NetworkState
}