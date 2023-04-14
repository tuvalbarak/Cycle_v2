package com.tdp.cycle.common

import com.tdp.cycle.models.cycle_server.Driver

object DriverPreferencesConsts {

    const val pushNotifications = "push_notifications"
    const val multipleChargingStops = "multiple_charging_stops"
    const val allowTollRoads = "allow_toll_roads"

    const val lastSelectedEV = "last_selected_ev"

    var currentDriver: Driver? = null

}