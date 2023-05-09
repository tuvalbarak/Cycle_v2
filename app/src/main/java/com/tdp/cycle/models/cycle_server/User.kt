package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long? = null,
    val googleId: String? = null,
    val email: String? = null,
    val name: String? = null,
    val thumbnail: String? = null,
    val phone: String? = null,
    val crystalsBalance: Int? = null,
    val electricVehicles: List<ElectricVehicle>? = null,
    val myElectricVehicle: Long? = null,
    val userPreferance: UserPreferance? = null,
    val drivingCharacteristic: DrivingCharacteristic? = null,
    val station: ChargingStation? = null,
    val gamifications: List<Gamification>? = null
) : Parcelable

data class UserRequest(
    val googleId: String? = null,
    val email: String? = null,
    val name: String? = null,
    val thumbnail: String? = null,
    val phone: String? = null,
    val crystalsBalance: Int? = null,
//    val vehiclesHistory: List<Long>? = null,
    val my_electric_vehicle: Long? = null,
//    val userPreferance: UserPreferance? = null,
    val station: ChargingStation? = null,
)