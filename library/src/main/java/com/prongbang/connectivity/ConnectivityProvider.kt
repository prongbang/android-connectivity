package com.prongbang.connectivity

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle

abstract class ConnectivityProvider : DefaultLifecycleObserver {

    abstract fun addListener(listener: ConnectivityStateListener)
    abstract fun removeListener(listener: ConnectivityStateListener)
    abstract fun getNetworkState(): NetworkState

    companion object {
        fun create(
            context: Context,
            lifecycle: Lifecycle,
            stateListener: ConnectivityStateListener
        ): ConnectivityProvider {
            return create(context, stateListener).apply {
                lifecycle.addObserver(this)
            }
        }

        fun create(
            context: Context,
            stateListener: ConnectivityStateListener
        ): ConnectivityProvider {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ConnectivityProviderImpl(cm, stateListener)
            } else {
                ConnectivityProviderLegacyImpl(
                    context,
                    cm,
                    NetworkStateConnectionImpl(cm),
                    stateListener
                )
            }
        }
    }
}