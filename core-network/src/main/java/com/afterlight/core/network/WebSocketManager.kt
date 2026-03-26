package com.afterlight.core.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

/**
 * WebSocket connection manager with exponential backoff reconnection.
 * Stage 13: Real-time party updates, heartbeat strategy.
 */
@Singleton
class WebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    
    companion object {
        private const val TAG = "WebSocketManager"
        private const val NORMAL_CLOSURE_STATUS = 1000
        private const val HEARTBEAT_INTERVAL_MS = 30_000L
        private const val MAX_RECONNECT_DELAY_MS = 60_000L
        private const val INITIAL_RECONNECT_DELAY_MS = 1_000L
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    private var webSocket: WebSocket? = null
    private var reconnectAttempts = 0
    private var shouldReconnect = false
    private var wsUrl: String? = null
    
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _messages = MutableStateFlow<String?>(null)
    val messages: StateFlow<String?> = _messages.asStateFlow()
    
    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }
    
    /**
     * Connects to WebSocket server with automatic reconnection.
     */
    fun connect(url: String) {
        wsUrl = url
        shouldReconnect = true
        reconnectAttempts = 0
        connectInternal()
    }
    
    /**
     * Disconnects from WebSocket server and stops reconnection attempts.
     */
    fun disconnect() {
        shouldReconnect = false
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Client disconnect")
        webSocket = null
        _connectionState.value = ConnectionState.Disconnected
    }
    
    /**
     * Sends a message through the WebSocket connection.
     */
    fun sendMessage(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }
    
    private fun connectInternal() {
        val url = wsUrl ?: return
        
        _connectionState.value = ConnectionState.Connecting
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket connected: $url")
                _connectionState.value = ConnectionState.Connected
                reconnectAttempts = 0
                startHeartbeat()
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "WebSocket message received: $text")
                _messages.value = text
            }
            
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $code - $reason")
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closed: $code - $reason")
                _connectionState.value = ConnectionState.Disconnected
                
                if (shouldReconnect) {
                    scheduleReconnect()
                }
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket failure: ${t.message}", t)
                _connectionState.value = ConnectionState.Error(t.message ?: "Unknown error")
                
                if (shouldReconnect) {
                    scheduleReconnect()
                }
            }
        })
    }
    
    /**
     * Schedules reconnection with exponential backoff.
     */
    private fun scheduleReconnect() {
        val delay = calculateReconnectDelay()
        reconnectAttempts++
        
        Log.d(TAG, "Scheduling reconnect attempt $reconnectAttempts in ${delay}ms")
        
        scope.launch {
            delay(delay)
            if (shouldReconnect) {
                connectInternal()
            }
        }
    }
    
    /**
     * Calculates exponential backoff delay with jitter.
     */
    private fun calculateReconnectDelay(): Long {
        val exponentialDelay = INITIAL_RECONNECT_DELAY_MS * 2.0.pow(reconnectAttempts - 1).toLong()
        val cappedDelay = min(exponentialDelay, MAX_RECONNECT_DELAY_MS)
        val jitter = (Math.random() * 1000).toLong()
        return cappedDelay + jitter
    }
    
    /**
     * Starts heartbeat to keep connection alive.
     */
    private fun startHeartbeat() {
        scope.launch {
            while (_connectionState.value == ConnectionState.Connected) {
                delay(HEARTBEAT_INTERVAL_MS)
                webSocket?.send("ping") ?: break
            }
        }
    }
}
