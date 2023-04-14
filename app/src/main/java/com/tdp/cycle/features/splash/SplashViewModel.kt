package com.tdp.cycle.features.splash

import com.google.firebase.auth.FirebaseAuth
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.DriverPreferencesConsts.currentDriver
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.User
import com.tdp.cycle.models.cycle_server.Driver
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    enum class NavigationEvent {
        GO_TO_SIGN_IN,
        GO_TO_SIGN_UP,
        GO_TO_HOME
    }

    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    fun handleAuth(auth: FirebaseAuth?) {
        auth?.currentUser?.let { firebaseUser ->
            safeViewModelScopeIO {
                //startProgress
                val user = User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName,
                    phoneNumber = firebaseUser.phoneNumber,
                    email = firebaseUser.email,
                    photoUrl = firebaseUser.photoUrl.toString()
                )
                userRepository.upsertUser(user)
                currentDriver = Driver(
                    id = 111,
                    email = user.email,
                    name = user.name,
                    phone = user.phoneNumber,
                )
                navigationEvent.postRawValue(NavigationEvent.GO_TO_HOME)
                //endProgress
            }
        } ?: run {
            navigationEvent.postRawValue(NavigationEvent.GO_TO_SIGN_IN)
        }
    }

}