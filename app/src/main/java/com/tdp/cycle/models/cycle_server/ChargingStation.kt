package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargingStation(
    val id: Long? = null,
    val name: String? = null,
    val lat: Float? = null,
    val lng: Float? = null,
    val provider: String? = null,
    val priceDetails: String? = null,
    val address: String? = null,
    val city: String? = null,
    val count: Int? = null,
    val power: Float? = null,
    val connectorType: String? = null,
    val condition: String? = null,
    val isPrivate: Boolean? = null,
    val ratings: List<Float?>? = null,
    val comments: List<Comment?>? = null,
    val owner: User? = null,
    var distanceFromRoute: Double? = null,
) : Parcelable

data class ChargingStationRequest(
    val name: String,
    val lat: Float,
    val lng: Float,
    val provider: String,
    val priceDetails: String,
    val address: String,
    val city: String,
    val count: Float,
    val power: Float,
    val connectorType: String,
    val condition: String,
    val isPrivate: Boolean,
    val ownerId: Long
)