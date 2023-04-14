package com.tdp.cycle.repositories

import com.google.android.gms.maps.model.LatLng
import com.tdp.cycle.features.routes.toLatLngFormat
import com.tdp.cycle.models.responses.Location
import com.tdp.cycle.remote.IWeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: IWeatherService
){

    suspend fun getWeather(location: Location) = weatherService.getWeather(
        latLng = location.toLatLngFormat(),
        apiKey = "563abb079dfb49dcadc191831232201"
    )

}