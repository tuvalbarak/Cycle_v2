package com.tdp.cycle.models.cycle_server

data class ElectricVehicle(
    val id: Long?,
    val currentBatteryId: Long?,
    val vehicleMetaId: Long?,
    var isSelected: Boolean? = false
)

data class ElectricVehicleRequest(
    val current_battery_id: Long?,
    val vehicle_meta_id: Long?,
)