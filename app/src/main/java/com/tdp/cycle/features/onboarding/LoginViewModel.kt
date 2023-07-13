package com.tdp.cycle.features.onboarding

import com.google.firebase.auth.FirebaseUser
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.cycle_server.AuthRequest
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : CycleBaseViewModel() {

    enum class NavigationEvent {
        GO_TO_HOME
    }

    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    fun auth(firebaseUser: FirebaseUser?) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val authRequest = AuthRequest(
                google_id = firebaseUser?.uid ?: "",
                email = firebaseUser?.email ?: "",
                name = firebaseUser?.displayName ?: "",
                phone = firebaseUser?.phoneNumber ?: ""
            )
            when(authRepository.auth(authRequest)) {
                is RemoteResponseSuccess -> navigationEvent.postRawValue(NavigationEvent.GO_TO_HOME)
                is RemoteResponseError -> { }
                else -> { }
            }
            progressData.endProgress()
        }
    }

    fun loginWithEmail(email: String) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val authRequest = AuthRequest(
                google_id = System.currentTimeMillis().toString(),
                email = email,
                name = "Cycle",
                phone = "asdasd"
            )
            when(authRepository.auth(authRequest)) {
                is RemoteResponseSuccess -> navigationEvent.postRawValue(NavigationEvent.GO_TO_HOME)
                is RemoteResponseError -> {}
                else -> {}
            }
            progressData.endProgress()
        }
    }

}