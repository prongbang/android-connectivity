package com.prongbang.connectivity

import android.os.Handler
import android.os.Looper

abstract class ConnectivityProviderBaseImpl : ConnectivityProvider() {
    private val handler = Handler(Looper.getMainLooper())
    private val listeners = mutableSetOf<ConnectivityStateListener>()
    private var subscribed = false

    protected abstract fun subscribe()
    protected abstract fun unsubscribe()

    override fun addListener(listener: ConnectivityStateListener) {
        listeners.add(listener)
        listener.onNetworkStateChange(getNetworkState())
        verifySubscription()
    }

    override fun removeListener(listener: ConnectivityStateListener) {
        listeners.remove(listener)
        verifySubscription()
    }

    private fun verifySubscription() {
        if (!subscribed && listeners.isNotEmpty()) {
            subscribe()
            subscribed = true
        } else if (subscribed && listeners.isEmpty()) {
            unsubscribe()
            subscribed = false
        }
    }

    protected fun dispatchChange(state: NetworkState) {
        handler.post {
            for (listener in listeners) {
                listener.onNetworkStateChange(state)
            }
        }
    }
}