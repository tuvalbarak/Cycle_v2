package com.tdp.cycle.repositories

import android.content.SharedPreferences
import com.tdp.cycle.models.cycle_server.AuthRequest
import com.tdp.cycle.remote.ICycleService
import com.tdp.cycle.remote.networking.RemoteResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val cycleService: ICycleService,
    private var sharedPreferences: SharedPreferences,
    private val remoteResponseHandler: RemoteResponseHandler
) {

    suspend fun auth(authRequest: AuthRequest) =
        remoteResponseHandler.safeApiCall {
            cycleService.auth(authRequest)
        }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().clear()
        }
    }

}