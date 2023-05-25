package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreferance (
    val id: Long? = null,
    var areNotificationAllowed: Boolean? = null,
    var areTollRoadsAllowed: Boolean? = null,
    var areMultipleChargingStopsAllowed: Boolean? = null,
    var arePrivateChargingStationsAllowed: Boolean? = null,
    val roadLandscape: String? = null,
) : Parcelable