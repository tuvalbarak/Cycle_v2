package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Battery(
    val id: Long?,
    val rangeCapacity: Int?,
    val batteryCapacity: Int?,
    var percentage: Int?,
    val consumptionPerKm: Int
) : Parcelable