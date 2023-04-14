package com.tdp.cycle.remote

import com.tdp.cycle.models.responses.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherService {

    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") latLng: String
    ): retrofit2.Response<WeatherResponse>

}