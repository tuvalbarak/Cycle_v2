package com.tdp.cycle.models

import com.google.gson.annotations.SerializedName
import com.tdp.cycle.models.responses.Location

data class ChargingStationModel(
    val id: Long?,
    val stationName: String?,
    val stationOperator: String?,
    val location: Location?,
    val stationAccess: StationAccess?
)

enum class StationAccess(val value: String) {
    PRIVATE(value = "private"),
    PUBLIC(value = "public")
}

data class ChargingStationRealModel(
    val station_id: Long? = null,
    val station_name: String? = null,
//    @SerializedName("provider_id") val providerId: String? = null,
//    @SerializedName("price") val price: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
//    @SerializedName("city") val city: String? = null,
//    @SerializedName("address") val address: String? = null,
//    @SerializedName("connector_type") val connectorType: Int? = null,
//    @SerializedName("count") val count: Int? = null,
//    @SerializedName("power") val power: Int? = null,
    val station_access: String? = null,
    var distanceFromRoute: Double? = null,
    var distanceFromOrigin: Double? = null
) {
    fun getLocation() = Location(lat, lng)
}