package com.tdp.cycle.features.profile.my_charging_stations

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import com.tdp.cycle.models.cycle_server.User
import com.tdp.cycle.remote.networking.LocalResponseError
import com.tdp.cycle.remote.networking.LocalResponseSuccess
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.ChargingStationsRepository
import com.tdp.cycle.repositories.MapsRepository
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyChargingStationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chargingStationRepository: ChargingStationsRepository,
    private val mapsRepository: MapsRepository
) : CycleBaseViewModel() {

    val myChargingStation = MutableLiveData<ChargingStation?>()
    val navigationEvent = SingleLiveEvent<NavigationEvent>()
    val user = MutableLiveData<User?>()
    val chargingStationUpdated = MutableLiveData<Boolean>()

    enum class NavigationEvent {
        GO_TO_MY_CHARGING_STATION,
        GO_TO_CREATE_CHARGING_STATION
    }

    init {
        getUser()
    }

    private fun getUser() {
        safeViewModelScopeIO {
            progressData.startProgress()
            when(val response = userRepository.getUserMe()) {
                is RemoteResponseSuccess -> user.postValue(response.data)
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
            progressData.endProgress()
        }
    }

    fun createChargingStation(chargingStationRequest: ChargingStationRequest) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val addressResponse = mapsRepository.getGeocodeByAddress(chargingStationRequest.address + chargingStationRequest.city)
            if(addressResponse.isSuccessful) {
                val ownerId = user.value?.id ?: 0
                val location = addressResponse.body()?.mapsGeocodeResults?.firstOrNull()?.geometry?.location
                val lat = location?.lat?.toFloat() ?: 0f
                val lng = location?.lng?.toFloat() ?: 0f
                val request = chargingStationRequest.copy(
                    ownerId = ownerId,
                    lat = lat,
                    lng = lng
                )
                when(val response = chargingStationRepository.createChargingStation(request)) {
                    is RemoteResponseSuccess -> {
                        myChargingStation.postValue(response.data)
                        navigationEvent.postRawValue(NavigationEvent.GO_TO_MY_CHARGING_STATION)
                    }
                    is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                    else -> { }
                }
            }
            progressData.endProgress()
        }
    }

    fun updateChargingStation(chargingStationRequest: ChargingStationRequest) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val addressResponse = mapsRepository.getGeocodeByAddress(chargingStationRequest.address + chargingStationRequest.city)
            if(addressResponse.isSuccessful) {
                val ownerId = user.value?.id ?: 0
                val location = addressResponse.body()?.mapsGeocodeResults?.firstOrNull()?.geometry?.location
                val lat = location?.lat?.toFloat() ?: 0f
                val lng = location?.lng?.toFloat() ?: 0f
                val request = chargingStationRequest.copy(
                    ownerId = ownerId,
                    lat = lat,
                    lng = lng
                )
                when(val response = chargingStationRepository.updateChargingStation(request)) {
                    is RemoteResponseSuccess -> {
                        myChargingStation.postValue(response.data)
                        chargingStationUpdated.postValue(true)
                    }
                    is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                    else -> { }
                }
            }
            progressData.endProgress()
        }
    }


}