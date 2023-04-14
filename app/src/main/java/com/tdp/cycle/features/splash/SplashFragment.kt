package com.tdp.cycle.features.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: CycleBaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()


    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        splashViewModel.handleAuth(auth)
        initObservers()
    }

    private fun initObservers() {
        splashViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { navigation ->
                val action = when(navigation) {
                    SplashViewModel.NavigationEvent.GO_TO_SIGN_IN -> SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    SplashViewModel.NavigationEvent.GO_TO_SIGN_UP -> TODO()
                    SplashViewModel.NavigationEvent.GO_TO_HOME -> SplashFragmentDirections.actionSplashFragmentToRoutesFragment()
                }
                findNavController().safeNavigate(action)
            }
        }
    }

    companion object {
        private const val TAG = "SplashFragmentTAG"
    }
}