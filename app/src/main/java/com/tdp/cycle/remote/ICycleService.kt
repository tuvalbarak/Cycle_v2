package com.tdp.cycle.remote

import com.tdp.cycle.models.UserServer
import com.tdp.cycle.models.cycle_server.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ICycleService {

    /** Drivers **/
    @GET("drivers")
    suspend fun getDrivers(): ServerResponse<List<Driver?>>

    @GET("drivers/{id}")
    suspend fun getDriverById(
        @Path(value = "id") driverId: Long
    ): ServerResponse<Driver?>

    @POST("drivers")
    suspend fun createDriver(
        @Body driver: Driver
    ): ServerResponse<Driver?>

    @PATCH("drivers/{id}")
    suspend fun updateDriver(
        @Path(value = "id") driverId: Long,
        @Body driver: Driver
    ): ServerResponse<Driver?>

    /** Vehicles Meta **/
    @GET("vehiclesMeta")
    suspend fun getVehiclesMeta(): ServerResponse<List<VehicleMeta?>?>

    @GET("vehiclesMeta/{id}")
    suspend fun getVehicleById(
        @Path(value = "id") vehicleMetaId: Long
    ): ServerResponse<VehicleMeta?>

    @POST("vehiclesMeta")
    suspend fun createVehicleMeta(
        @Body vehicleMeta: VehicleMeta
    ): ServerResponse<VehicleMeta?>


    /** Batteries **/
    @GET("batteries")
    suspend fun getBatteries(): ServerResponse<List<Battery?>?>

    @POST("batteries")
    suspend fun createBattery(
        @Body battery: Battery
    ): ServerResponse<Battery?>


    /** Electric Vehicles **/
    @GET("electricVehicles")
    suspend fun getElectricVehicles(): ServerResponse<List<ElectricVehicle?>?>

    @GET("electricVehicles/{id}")
    suspend fun getElectricVehiclesById(
        @Path(value = "id") electricVehicleId: Long
    ): ServerResponse<ElectricVehicle?>

    @POST("electricVehicles")
    suspend fun createElectricVehicle(
        @Body electricVehicle: ElectricVehicleRequest
    ): ServerResponse<ElectricVehicle?>
}