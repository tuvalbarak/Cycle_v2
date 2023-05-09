package com.tdp.cycle.features.profile.gamification

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.models.cycle_server.Gamification
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    val gamifications = MutableLiveData<List<Gamification>?>()

    init {
        getGamifications()
    }

    private fun getGamifications() {
        safeViewModelScopeIO {
            when(val response = userRepository.getUserMe()) {
                is RemoteResponseSuccess -> gamifications.postValue(response.data?.gamifications)
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
        }
    }

}