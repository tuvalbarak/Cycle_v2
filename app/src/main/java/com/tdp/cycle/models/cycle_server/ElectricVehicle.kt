package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ElectricVehicle(
    val id: Long?,
    val battery: Battery?,
    val vehicleMeta: VehicleMeta?,
    val ownerId: Long?,
    var isSelected: Boolean? = false
) : Parcelable

data class ElectricVehicleRequest(
    val current_battery_id: Long?,
    val vehicle_meta_id: Long?,
    val owner_id: Long?
)