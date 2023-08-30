package com.tdp.cycle.features.splash

import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    enum class NavigationEvent {
        GO_TO_SIGN_IN,
        GO_TO_HOME
    }

    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    init {
        getUser()
    }

    private fun getUser() {
        safeViewModelScopeIO {
            val navigation = when(val response = userRepository.getUserMe()) {
                is RemoteResponseSuccess -> NavigationEvent.GO_TO_HOME
                is RemoteResponseError -> {
                    errorEvent.postRawValue(response.error.getErrorMsgByType())
                    NavigationEvent.GO_TO_SIGN_IN
                }
                else -> NavigationEvent.GO_TO_SIGN_IN
            }
            navigationEvent.postRawValue(navigation)
        }
    }

//    fun handleAuth(auth: FirebaseAuth?) {
//        auth?.currentUser?.let { firebaseUser ->
//            safeViewModelScopeIO {
//                //startProgress
////                val user = User(
////                    id = firebaseUser.uid,
////                    name = firebaseUser.displayName,
////                    phoneNumber = firebaseUser.phoneNumber,
////                    email = firebaseUser.email,
////                    photoUrl = firebaseUser.photoUrl.toString()
////                )
////                userRepositoryDepricated.upsertUser(user)
////                currentDriver = Driver(
////                    id = 111,
////                    email = user.email,
////                    name = user.name,
////                    phone = user.phoneNumber,
////                )
//                navigationEvent.postRawValue(NavigationEvent.GO_TO_HOME)
//                //endProgress
//            }
//        } ?: run {
//            navigationEvent.postRawValue(NavigationEvent.GO_TO_SIGN_IN)
//        }
//    }

}