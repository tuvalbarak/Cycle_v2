package com.tdp.cycle.remote.networking

import android.content.SharedPreferences
import android.util.Log
import com.tdp.cycle.common.Consts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.locks.ReentrantReadWriteLock

class AuthInterceptor(
    private val apiSettings: ApiSettings,
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    private var storedAuthToken: String? = null

    private val lock = ReentrantReadWriteLock(true)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken: String?
        var originalRequest = chain.request()
        lock.readLock().lock()
        try {
            authToken = getStoredAuthToken()
            runBlocking(Dispatchers.IO) {
                launch {
                    sharedPreferences.edit().putString(Consts.USER_AUTH_TOKEN, authToken).apply()
                }
            }
        } finally {
            lock.readLock().unlock()
        }

        originalRequest = addTokenToRequest(originalRequest, authToken)
        var response = chain.proceed(originalRequest)

        if (!response.header("X-Auth-Token").isNullOrEmpty()) {
            val newAuthToken = response.header("X-Auth-Token")
            updateAuthToken(newAuthToken)
        }

        return response
    }

    private fun updateAuthToken(newAuthToken: String?) {
        storedAuthToken = newAuthToken
        apiSettings.authToken = newAuthToken
    }

    private fun getStoredAuthToken(): String? = apiSettings.authToken

    private fun addTokenToRequest(request: Request, authToken: String?): Request {
        var request = request
        Log.d(TAG, "authToken: $authToken")
        authToken?.let {
            val requestBuilder = request.newBuilder()
                .addHeader("X-Auth-Token", "$authToken")
            request = requestBuilder.build()
        }
        return request
    }

    companion object {
        private const val TAG = "AuthInterceptorTAG"
    }
}