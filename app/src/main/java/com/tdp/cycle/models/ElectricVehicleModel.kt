package com.tdp.cycle.models

data class ElectricVehicleModel(
    val id: Long,
    val brand: String,
    val model: Model,
    val battery: Battery,
//    val owner: String,
    var isSelected: Boolean = false
)

data class Model(
    val name: String,
    val image: Int,
    val type: String,
    val year: Int
)

data class Battery(
    val rangeCapacity: Double,
    val batteryCapacity: Double,
    val consumptionPerKM: Double,
    var currentPercentage: Double
)
