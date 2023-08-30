package com.tdp.cycle.features.profile

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.cycle_server.User
import com.tdp.cycle.models.cycle_server.UserRequest
import com.tdp.cycle.remote.networking.LocalResponseError
import com.tdp.cycle.remote.networking.LocalResponseSuccess
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
        GO_TO_LOGIN,
        POP_BACKSTACK
    }

    init {
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

    fun logout() {
        safeViewModelScopeIO {
            progressData.startProgress()
            userRepository.logout()
            navigateTo(NavigationEvent.GO_TO_LOGIN)
            progressData.endProgress()
        }
    }

    fun onSaveButtonClicked(userRequest: UserRequest) {
        safeViewModelScopeIO {
            progressData.startProgress()
            when (val response = userRepository.updateUser(userRequest)) {
                is RemoteResponseSuccess -> {
                    user.postValue(response.data)
//                    navigationEvent.postRawValue(NavigationEvent.POP_BACKSTACK)
                }
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
            progressData.endProgress()
        }
    }

    fun navigateTo(navigation: NavigationEvent) {
        navigationEvent.postRawValue(navigation)
    }

}