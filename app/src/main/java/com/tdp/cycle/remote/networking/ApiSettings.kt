package com.tdp.cycle.remote.networking

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ApiSettings (private val sharedPreferences: SharedPreferences) {

    private val AUTH_TOKEN_KEY = "token_key"
    private val DEFAULT_TOKEN = null

    var authToken: String?
        get() = sharedPreferences.getString(AUTH_TOKEN_KEY, DEFAULT_TOKEN)
        set(authToken) = runBlocking(Dispatchers.IO) {
            launch {
                sharedPreferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply()
            }
        }
}