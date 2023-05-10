package com.tdp.cycle.models.cycle_server

enum class ChargingStationStatus(val value: String) {
    AVAILABLE(value = "Available"),
    BROKEN(value = "Broken"),
    OCCUPIED(value = "Occupied")
}