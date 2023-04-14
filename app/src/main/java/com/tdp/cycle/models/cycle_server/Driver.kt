package com.tdp.cycle.models.cycle_server

data class Driver(
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null,
    val thumbnail: String? = null,
    val phone: String? = null,
    val crystalsBalance: Int? = null,
    val drivingCharacteristicId: Int? = null,
    val preferenceId: Int? = null,
    val vehiclesHistory: List<Long>? = null,
    val lastVehicleUsedId: Long? = null
)