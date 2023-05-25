package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreferance (
    val id: Long? = null,
    val areNotificationAllowed: Boolean? = null,
    val areTollRoadsAllowed: Boolean? = null,
    val areMultipleChargingStopsAllowed: Boolean? = null,
    val arePrivateChargingStationsAllowed: Boolean? = null,
    val roadLandscape: String? = null,
) : Parcelable