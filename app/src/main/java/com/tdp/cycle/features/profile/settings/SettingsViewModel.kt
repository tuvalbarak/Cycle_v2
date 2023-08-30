package com.tdp.cycle.features.profile.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    val allowPushNotifications = MutableLiveData<Boolean>()
    val allowTollRoads = MutableLiveData<Boolean>()
    val allowMultipleChargingStops = MutableLiveData<Boolean>()
    val allowPrivateStations = MutableLiveData<Boolean>()

    init {
        getArePushNotificationsAllowed()
        getAreMultipleChargingStationsAllowed()
        getAreTollRoadsAllowed()
        getArePrivateStationsAllowed()
    }

    fun updatePushNotificationsInSP(pushNotifications: Boolean) {
        userRepository.updatePushNotificationsInSP(pushNotifications)
        getArePushNotificationsAllowed()
    }

    fun updateMultipleChargingStopsInSP(multipleChargingStops: Boolean) {
        userRepository.updateMultipleChargingStopsInSP(multipleChargingStops)
        getAreMultipleChargingStationsAllowed()
    }

    fun updatePushAllowTollRoadsInSP(allowTollRoads: Boolean) {
        userRepository.updatePushAllowTollRoadsInSP(allowTollRoads)
        getAreTollRoadsAllowed()
    }

    fun updateIsPrivateStationsAllowedInSp(isPrivateStationsAllowed: Boolean) {
        userRepository.updateIsPrivateStationsAllowedInSp(isPrivateStationsAllowed)
        getArePrivateStationsAllowed()
    }

    private fun getArePushNotificationsAllowed() {
        safeViewModelScopeIO {
            allowPushNotifications.postValue(userRepository.getArePushNotificationsAllowed())
        }
    }

    private fun getAreMultipleChargingStationsAllowed() {
        safeViewModelScopeIO {
            allowMultipleChargingStops.postValue(userRepository.getAreMultipleChargingStationsAllowed())
        }
    }

    private fun getAreTollRoadsAllowed() {
        safeViewModelScopeIO {
            allowTollRoads.postValue(userRepository.getAreTollRoadsAllowed())
        }
    }

    private fun getArePrivateStationsAllowed() {
        safeViewModelScopeIO {
            allowPrivateStations.postValue(userRepository.getArePrivateStationsAllowed())
        }
    }

}