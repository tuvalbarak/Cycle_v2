package com.tdp.cycle.features.profile.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdp.cycle.common.DriverPreferencesConsts
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val allowPushNotifications = MutableLiveData<Boolean>()
    val allowTollRoads = MutableLiveData<Boolean>()
    val allowMultipleChargingStops = MutableLiveData<Boolean>()

    init {
        getArePushNotificationsAllowed()
        getAreMultipleChargingStationsAllowed()
        getAreTollRoadsAllowed()
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

    private fun getArePushNotificationsAllowed() {
        viewModelScope.launch(Dispatchers.IO) {
            allowPushNotifications.postValue(userRepository.getArePushNotificationsAllowed())
        }
    }

    private fun getAreMultipleChargingStationsAllowed() {
        viewModelScope.launch(Dispatchers.IO) {
            allowMultipleChargingStops.postValue(userRepository.getAreMultipleChargingStationsAllowed())
        }
    }

    private fun getAreTollRoadsAllowed() {
        viewModelScope.launch(Dispatchers.IO) {
            allowTollRoads.postValue(userRepository.getAreTollRoadsAllowed())
        }
    }

}