package ru.babaetskv.passionwoman.app.utils

import android.content.Context
import android.net.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

class NetworkStateChecker(context: Context) : ConnectivityManager.NetworkCallback() {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkAvailabilityFlow = MutableStateFlow(isConnected)
    private val isConnected: Boolean
        get() = connectivityManager.allNetworks.mapNotNull {
            connectivityManager.getNetworkCapabilities(it)
        }.filter {
            it.hasCapability(REQUIRED_NETWORK_CAPABILITY)
        }.any { capabilities ->
            REQUIRED_NETWORK_TYPES.any { capabilities.hasTransport(it) }
        }

    val networkAvailabilityFlowDebounced: Flow<Boolean>
        get() = networkAvailabilityFlow.debounce(NETWORK_UPDATE_DELAY_MS)

    init {
        val request = NetworkRequest.Builder()
            .addCapability(REQUIRED_NETWORK_CAPABILITY)
            .apply {
                REQUIRED_NETWORK_TYPES.forEach {
                    addTransportType(it)
                }
            }
            .build()
        connectivityManager.requestNetwork(request, this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkAvailabilityFlow.value = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkAvailabilityFlow.value = false
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        networkAvailabilityFlow.value = !blocked
    }

    companion object {
        private const val NETWORK_UPDATE_DELAY_MS = 1000L
        private val REQUIRED_NETWORK_TYPES = listOf(
            NetworkCapabilities.TRANSPORT_WIFI,
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
        private const val REQUIRED_NETWORK_CAPABILITY = NetworkCapabilities.NET_CAPABILITY_INTERNET
    }
}
