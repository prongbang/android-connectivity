package com.prongbang.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO
import android.net.NetworkInfo
import androidx.lifecycle.LifecycleOwner

@Suppress("DEPRECATION")
class ConnectivityProviderLegacyImpl(
    private val context: Context,
    private val cm: ConnectivityManager,
    private val networkStateConnection: NetworkStateConnection,
    private val connectivityStateListener: ConnectivityStateListener
) : ConnectivityProviderBaseImpl() {

    private val receiver = ConnectivityReceiver()

    override fun onStart(owner: LifecycleOwner) {
        addListener(connectivityStateListener)
    }

    override fun onStop(owner: LifecycleOwner) {
        removeListener(connectivityStateListener)
    }

    override fun subscribe() {
        context.registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    override fun unsubscribe() {
        context.unregisterReceiver(receiver)
    }

    override fun getNetworkState(): NetworkState {
        val activeNetworkInfo = cm.activeNetworkInfo
        return if (activeNetworkInfo != null) {
            NetworkState.ConnectedState.ConnectedLegacy(activeNetworkInfo)
        } else {
            NetworkState.NotConnectedState
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            dispatchChange(networkStateConnection.getNetworkState(intent))
        }
    }
}