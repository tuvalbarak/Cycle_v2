package com.tdp.cycle.repositories

import android.content.SharedPreferences
import com.tdp.cycle.common.DriverPreferencesConsts
import com.tdp.cycle.common.isNull
import com.tdp.cycle.models.cycle_server.User
import com.tdp.cycle.models.cycle_server.UserRequest
import com.tdp.cycle.remote.ICycleService
import com.tdp.cycle.remote.networking.LocalResponseError
import com.tdp.cycle.remote.networking.LocalResponseSuccess
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseHandler
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.ResponseResult
import com.tdp.cycle.remote.networking.ResponseSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val cycleService: ICycleService,
    private val remoteResponseHandler: RemoteResponseHandler,
    private val sharedPreferences: SharedPreferences
) {

    private var currentUser: User? = null
    private var currentUserResponse: ResponseResult<User>? = null

    suspend fun updateUser(userRequest: UserRequest) = currentUser?.id?.let { userId ->
        val response = remoteResponseHandler.safeApiCall {
            cycleService.updateUser(userId, userRequest)
        }
        if(response is RemoteResponseSuccess) {
            currentUser = response.data?.copy()
            currentUserResponse = response
            (currentUserResponse as? RemoteResponseSuccess)?.data = currentUser
        }
        response
    }

    suspend fun getUserMe(fetchFromServer: Boolean = false): ResponseResult<User>? {
        if (currentUser.isNull() || fetchFromServer) {
            currentUserResponse = fetchUserMe()
        }
        return currentUserResponse
    }

    private suspend fun fetchUserMe(): ResponseResult<User> {
        remoteResponseHandler.safeApiCall {
            cycleService.getUserMe()
        }.also { response ->
            if(response is RemoteResponseSuccess) {
                currentUser = response.data?.copy()
                currentUserResponse = response
                (currentUserResponse as? RemoteResponseSuccess)?.data = currentUser
            }
            return response
        }
    }

    suspend fun logout() {
//        db.clearAllTables()
        sharedPreferences.edit().clear().apply()
    }

    fun updatePushNotificationsInSP(pushNotifications: Boolean) {
        sharedPreferences.edit().putBoolean(DriverPreferencesConsts.pushNotifications, pushNotifications).apply()
    }

    fun updateMultipleChargingStopsInSP(multipleChargingStops: Boolean) {
        sharedPreferences.edit().putBoolean(DriverPreferencesConsts.multipleChargingStops, multipleChargingStops).apply()
    }

    fun updatePushAllowTollRoadsInSP(allowTollRoads: Boolean) {
        sharedPreferences.edit().putBoolean(DriverPreferencesConsts.allowTollRoads, allowTollRoads).apply()
    }

    fun updateIsPrivateStationsAllowedInSp(isPrivateStationsAllowed: Boolean) {
        sharedPreferences.edit().putBoolean(DriverPreferencesConsts.isPrivateStations, isPrivateStationsAllowed).apply()
    }

    suspend fun getArePushNotificationsAllowed() = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(DriverPreferencesConsts.pushNotifications, false)
    }

    suspend fun getAreMultipleChargingStationsAllowed() = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(DriverPreferencesConsts.multipleChargingStops, false)
    }

    suspend fun getAreTollRoadsAllowed() = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(DriverPreferencesConsts.allowTollRoads, false)
    }

    suspend fun getArePrivateStationsAllowed() = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(DriverPreferencesConsts.isPrivateStations, false)
    }

}
