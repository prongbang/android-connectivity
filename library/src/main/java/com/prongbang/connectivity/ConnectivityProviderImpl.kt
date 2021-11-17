package com.prongbang.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner

@RequiresApi(Build.VERSION_CODES.N)
class ConnectivityProviderImpl(
    private val cm: ConnectivityManager,
    private val connectivityStateListener: ConnectivityStateListener
) : ConnectivityProviderBaseImpl() {

    private val networkCallback = ConnectivityCallback()

    override fun onStart(owner: LifecycleOwner) {
        addListener(connectivityStateListener)
    }

    override fun onStop(owner: LifecycleOwner) {
        removeListener(connectivityStateListener)
    }

    override fun subscribe() {
        cm.registerDefaultNetworkCallback(networkCallback)
    }

    override fun unsubscribe() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    override fun getNetworkState(): NetworkState {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return if (capabilities != null) {
            NetworkState.ConnectedState.Connected(capabilities)
        } else {
            NetworkState.NotConnectedState
        }
    }

    private inner class ConnectivityCallback : ConnectivityManager.NetworkCallback() {

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            dispatchChange(NetworkState.ConnectedState.Connected(capabilities))
        }

        override fun onLost(network: Network) {
            dispatchChange(NetworkState.NotConnectedState)
        }
    }
}