package com.tdp.cycle.remote

import com.tdp.cycle.models.cycle_server.*
import com.tdp.cycle.remote.networking.ServerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ICycleService {

    //Auth
    @POST("auth")
    suspend fun auth(
        @Body authRequest: AuthRequest
    ) : ServerResponse<User>


    //Batteries
    @GET("batteries")
    suspend fun getBatteries() : ServerResponse<List<Battery>>

    //Charging Stations
    @GET("chargingStations")
    suspend fun getChargingStations() : ServerResponse<List<ChargingStation>>

    @GET("chargingStations/{id}")
    suspend fun getChargingStationById(
        @Path(value = "id") stationId: Long
    ) : ServerResponse<ChargingStation>

    @POST("chargingStations")
    suspend fun createChargingStation(
        @Body chargingStationRequest: ChargingStationRequest
    ) : ServerResponse<ChargingStation>

    @POST("chargingStations/{chargingStationId}/comment")
    suspend fun postComment(
        @Path(value = "chargingStationId") stationId: Long?,
        @Body commentRequest: CommentRequest
    ) : ServerResponse<ChargingStation>

    @POST("chargingStations/{chargingStationId}/rating")
    suspend fun postRating(
        @Path(value = "chargingStationId") stationId: Long?,
        @Body rating: RatingsRequest
    ) : ServerResponse<ChargingStation>

    @POST("chargingStations/{chargingStationId}/status")
    suspend fun postStatus(
        @Path(value = "chargingStationId") stationId: Long?,
        @Body status: StatusRequest
    ) : ServerResponse<ChargingStation>

    @PATCH("chargingStations/{chargingStationId}")
    suspend fun updateChargingStation(
        @Path(value = "chargingStationId") stationId: Long?,
        @Body status: ChargingStationRequest
    ) : ServerResponse<ChargingStation>

    //Electric Vehicles
    @GET("electricVehicles")
    suspend fun getElectricVehicles() : ServerResponse<List<ElectricVehicle>>

    @GET("electricVehicles/{id}")
    suspend fun getElectricVehiclesById(
        @Path(value = "id") vehicleId: Long
    ) : ServerResponse<ElectricVehicle>

    @POST("electricVehicles")
    suspend fun createElectricVehicle(
        @Body electricVehicleRequest: ElectricVehicleRequest
    ): ServerResponse<ElectricVehicle>

    //Vehicles Meta
    @GET("vehiclesMeta")
    suspend fun getVehiclesMeta() : ServerResponse<List<VehicleMeta>>

    @GET("vehiclesMeta/{id}")
    suspend fun getVehicleMetaById(
        @Path(value = "id") metaId: Long
    ) : ServerResponse<VehicleMeta>

    //User
    @GET("users/me")
    suspend fun getUserMe(): ServerResponse<User>

    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path(value = "id") userId: Long,
        @Body userRequest: UserRequest
    ) : ServerResponse<User>

}