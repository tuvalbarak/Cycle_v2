package com.tdp.cycle.repositories

import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import com.tdp.cycle.models.cycle_server.CommentRequest
import com.tdp.cycle.models.cycle_server.RatingsRequest
import com.tdp.cycle.remote.ICycleService
import com.tdp.cycle.remote.networking.RemoteResponseHandler

class ChargingStationsRepository(
    private val cycleService: ICycleService,
    private val remoteResponseHandler: RemoteResponseHandler,
) {

    suspend fun postComment(stationId: Long, commentRequest: CommentRequest) = remoteResponseHandler.safeApiCall {
        cycleService.postComment(stationId, commentRequest)
    }

    suspend fun postRating(stationId: Long, rating: Int) = remoteResponseHandler.safeApiCall {
        cycleService.postRating(stationId, RatingsRequest(rating))
    }

    suspend fun createChargingStation(chargingStationRequest: ChargingStationRequest) =
        remoteResponseHandler.safeApiCall {
            cycleService.createChargingStation(chargingStationRequest)
        }

    suspend fun getChargingStations() = remoteResponseHandler.safeApiCall {
        cycleService.getChargingStations()
    }

    suspend fun getChargingStationById(stationId: Long) = remoteResponseHandler.safeApiCall {
        cycleService.getChargingStationById(stationId)
    }

}