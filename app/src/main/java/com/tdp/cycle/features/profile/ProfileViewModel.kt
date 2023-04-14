package com.tdp.cycle.features.profile

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.User
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    val user = MutableLiveData<User?>()
    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    enum class NavigationEvent {
        GO_TO_PERSONAL_INFORMATION,
        GO_TO_MY_VEHICLES,
        GO_TO_MY_CHARGING_STATIONS,
        GO_TO_GAMIFICATION_SETTINGS,
        GO_TO_ACCOUNT,
        GO_TO_LOGIN
    }

    init {
        safeViewModelScopeIO {
            progressData.startProgress()
            user.postValue(userRepository.getUser())
            progressData.endProgress()
        }
    }

    fun logout() {
        safeViewModelScopeIO {
            progressData.startProgress()
            userRepository.logout()
            navigateTo(NavigationEvent.GO_TO_LOGIN)
            progressData.endProgress()
        }
    }

    fun navigateTo(navigation: NavigationEvent) {
        navigationEvent.postRawValue(navigation)
    }

}