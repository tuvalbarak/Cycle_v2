package com.tdp.cycle.repositories

import android.content.SharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.tdp.cycle.common.DriverPreferencesConsts
import com.tdp.cycle.common.DriverPreferencesConsts.lastSelectedEV
import com.tdp.cycle.local.CycleDB
import com.tdp.cycle.models.ElectricVehicleModel
import com.tdp.cycle.models.User
import com.tdp.cycle.models.cycle_server.Driver
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.VehicleMeta
import com.tdp.cycle.remote.ICycleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val db: CycleDB,
    private val sharedPReferences: SharedPreferences,
    private val cycleService: ICycleService
) {

    /**
     * To update user in Firebase:
     *  val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User profile updated.")
            }
        }
     * */


    suspend fun insertUser(user: User) {
        db.userDao().insertUser(user)
    }

    suspend fun upsertUser(user: User) {
        db.userDao().upsertUser(user)
    }

    suspend fun updateUser(user: User) {
        db.userDao().updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        db.userDao().deleteUser(user)
    }

    suspend fun getUser() = db.userDao().getUser()

    suspend fun logout() {
        Firebase.auth.signOut()
        db.clearAllTables()
    }

    fun updatePushNotificationsInSP(pushNotifications: Boolean) {
        sharedPReferences.edit().putBoolean(DriverPreferencesConsts.pushNotifications, pushNotifications).apply()
    }

    fun updateMultipleChargingStopsInSP(multipleChargingStops: Boolean) {
        sharedPReferences.edit().putBoolean(DriverPreferencesConsts.multipleChargingStops, multipleChargingStops).apply()
    }

    fun updatePushAllowTollRoadsInSP(allowTollRoads: Boolean) {
        sharedPReferences.edit().putBoolean(DriverPreferencesConsts.allowTollRoads, allowTollRoads).apply()
    }

    suspend fun getArePushNotificationsAllowed() = withContext(Dispatchers.IO) {
        sharedPReferences.getBoolean(DriverPreferencesConsts.pushNotifications, false)
    }

    suspend fun getAreMultipleChargingStationsAllowed() = withContext(Dispatchers.IO) {
        sharedPReferences.getBoolean(DriverPreferencesConsts.multipleChargingStops, false)
    }

    suspend fun getAreTollRoadsAllowed() = withContext(Dispatchers.IO) {
        sharedPReferences.getBoolean(DriverPreferencesConsts.allowTollRoads, false)
    }

    suspend fun updateMyVehiclesList(electricVehicle: ElectricVehicle?) {
        val updatedList = mutableListOf(1L, 2L, 3L)
        electricVehicle?.vehicleMetaId?.let {
            updatedList.add(it)
            cycleService.updateDriver(
                1,
                Driver(
                    name = "Tuval Barak",
                    phone = "972544873078",
                    crystalsBalance = 20000,
                    email = "baraktuval@gmail.com",
                    lastVehicleUsedId = 2,
                    vehiclesHistory = updatedList
                )
            )
        }

    }

    suspend fun updateLastSelectedEV(lastSelectedEV: VehicleMeta) {
        withContext(Dispatchers.IO) {
            Gson().toJson(lastSelectedEV)?.let {
                sharedPReferences.edit().putString(DriverPreferencesConsts.lastSelectedEV, it).apply()
            }
        }
    }

    suspend fun getLastSelectedEV() = withContext(Dispatchers.IO) {
        try {
            val electricVehicleJson = sharedPReferences.getString(lastSelectedEV, "")
            val state = Gson().fromJson(electricVehicleJson, VehicleMeta::class.java)
            state
        } catch (e: Exception) {
            null
        }
    }

}
