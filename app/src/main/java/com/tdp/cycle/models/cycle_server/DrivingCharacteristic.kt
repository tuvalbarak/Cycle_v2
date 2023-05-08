package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrivingCharacteristic(
    val id: Long? = null,
    val breakesUsageAverage: Float? = null,
    val speedAverage: Float? = null,
    val airConditionsAverage: Float? = null,
) : Parcelable