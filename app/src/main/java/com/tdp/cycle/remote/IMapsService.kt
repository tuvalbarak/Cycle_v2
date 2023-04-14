package com.tdp.cycle.remote

import com.tdp.cycle.models.responses.MapsDirectionsResponse
import com.tdp.cycle.models.responses.MapsElevationResponse
import com.tdp.cycle.models.responses.MapsGeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IMapsService {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("destination") destination: String,
        @Query("origin") origin: String,
        @Query("alternatives") alternatives: Boolean,
        @Query("waypoints") waypoints: String,
        @Query("key") key: String
    ): retrofit2.Response<MapsDirectionsResponse>

    @GET("geocode/json")
    suspend fun getGeocode(
        @Query("latlng") latlng: String,
        @Query("key") key: String
    ): retrofit2.Response<MapsGeocodeResponse>

    @GET("elevation/json")
    suspend fun getElevation(
        @Query("locations") location: String,
        @Query("key") key: String
    ): retrofit2.Response<MapsElevationResponse>
}