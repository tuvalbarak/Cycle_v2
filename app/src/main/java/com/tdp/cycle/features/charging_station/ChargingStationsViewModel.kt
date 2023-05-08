package com.tdp.cycle.features.charging_station

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.Comment
import com.tdp.cycle.models.cycle_server.CommentRequest
import com.tdp.cycle.remote.networking.LocalResponseError
import com.tdp.cycle.remote.networking.LocalResponseSuccess
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.ResponseResult
import com.tdp.cycle.remote.networking.ResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.ChargingStationsRepository
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChargingStationsViewModel @Inject constructor(
    private val chargingStationsRepository: ChargingStationsRepository,
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    val chargingStation = MutableLiveData<ChargingStation?>()
    val commentPosted = MutableLiveData<Boolean>()
    val ratingPosted = MutableLiveData<Boolean>()

    fun updateChargingStation(chargingStation: ChargingStation?) {
        safeViewModelScopeIO {
            progressData.startProgress()
            this.chargingStation.postValue(chargingStation)
            progressData.endProgress()
        }
    }

    fun postComment(text: String) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val user = userRepository.getUserMe() as? RemoteResponseSuccess
            val commentRequest = CommentRequest(
                content = text,
                commentator = user?.data?.name ?: "N/A"
            )
            chargingStation.value?.id?.let {
                when(val response = chargingStationsRepository.postComment(it, commentRequest)) {
                    is RemoteResponseSuccess -> {
                        commentPosted.postValue(true)
                        chargingStation.postValue(response.data)
                    }
                    is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                    else -> { }
                }

            }
            progressData.endProgress()
        }
    }

    fun postRating(rating: Int) {
        safeViewModelScopeIO {
            progressData.startProgress()
            chargingStation.value?.id?.let {
                when(val response = chargingStationsRepository.postRating(it, rating)) {
                    is RemoteResponseSuccess -> {
                        ratingPosted.postValue(true)
                        chargingStation.postValue(response.data)
                    }
                    is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                    else -> { }
                }
            }
            progressData.endProgress()
        }
    }

}