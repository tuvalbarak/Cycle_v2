package com.tdp.cycle.models.cycle_server

data class Battery(
    val id: Long?,
    val rangeCapacity: Int?,
    val batteryCapacity: Int?,
    var percentage: Int?,
    val consumptionPerKm: Int
)