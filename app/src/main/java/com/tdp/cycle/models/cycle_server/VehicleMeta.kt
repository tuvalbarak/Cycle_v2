package com.tdp.cycle.models.cycle_server

data class VehicleMeta (
    val id: Long?,
    val manufactureBatteryId: Long?,
    val brand: String?,
    val model: String?,
    val image: String?,
    val year: Int?,
    val vehicles: ArrayList<ElectricVehicle?>?,
    var isSelected: Boolean? = false
)

