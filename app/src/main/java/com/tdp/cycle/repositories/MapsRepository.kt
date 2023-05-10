package com.tdp.cycle.repositories

import com.tdp.cycle.remote.IMapsService
import javax.inject.Inject

class MapsRepository @Inject constructor(
    private val mapsService: IMapsService
){

    suspend fun getDirections(destination: String, origin: String, waypoints: String = "") = mapsService.getDirections(
        destination = destination,
        origin = origin,
        alternatives = true,
        waypoints = waypoints,
        key = "AIzaSyAJIBntjoplGTf0G5yqAKUr_5xbiARll4Y"
    )

    suspend fun getGeocodeByLatLng(latLng: String) = mapsService.getGeocodeByLatLng(
        latlng = latLng,
        key = "AIzaSyAJIBntjoplGTf0G5yqAKUr_5xbiARll4Y"
    )

    suspend fun getGeocodeByAddress(address: String) = mapsService.getGeocodeByAddress(
        address = address,
        key = "AIzaSyAJIBntjoplGTf0G5yqAKUr_5xbiARll4Y"
    )

    suspend fun getElevation(location: String) = mapsService.getElevation(
        location = location,
        key = "AIzaSyAJIBntjoplGTf0G5yqAKUr_5xbiARll4Y"
    )

}