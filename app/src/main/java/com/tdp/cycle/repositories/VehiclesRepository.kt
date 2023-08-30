package com.tdp.cycle.repositories

import com.tdp.cycle.models.cycle_server.ElectricVehicleRequest
import com.tdp.cycle.remote.ICycleService
import com.tdp.cycle.remote.networking.RemoteResponseHandler

class VehiclesRepository(
    private val cycleService: ICycleService,
    private val remoteResponseHandler: RemoteResponseHandler,
) {

    suspend fun getVehicleMetaById(metaId: Long) = remoteResponseHandler.safeApiCall {
        cycleService.getVehicleMetaById(metaId)
    }

    suspend fun getVehiclesMeta() = remoteResponseHandler.safeApiCall {
        cycleService.getVehiclesMeta()
    }

    suspend fun getElectricVehiclesById(vehicleId: Long) = remoteResponseHandler.safeApiCall {
        cycleService.getElectricVehiclesById(vehicleId)
    }

    suspend fun getElectricVehicles() = remoteResponseHandler.safeApiCall {
        cycleService.getElectricVehicles()
    }

    suspend fun createElectricVehicle(electricVehicleRequest: ElectricVehicleRequest) =
        remoteResponseHandler.safeApiCall {
            cycleService.createElectricVehicle(electricVehicleRequest)
        }

}
