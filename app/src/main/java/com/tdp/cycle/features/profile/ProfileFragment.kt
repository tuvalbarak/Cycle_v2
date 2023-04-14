package com.tdp.cycle.features.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: CycleBaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservers()
    }

    private fun initUi() {
        initListeners()
    }

    private fun initObservers() {
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                profileFullName.text = user?.name ?: ""
                profileUserEmail.text = user?.email ?: ""
//                profileUserImage
            }
        }

        profileViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

        profileViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { navigationEvent ->
                val action = when(navigationEvent) {
                    ProfileViewModel.NavigationEvent.GO_TO_LOGIN -> ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                    ProfileViewModel.NavigationEvent.GO_TO_PERSONAL_INFORMATION -> ProfileFragmentDirections.actionProfileFragmentToPersonalInformationFragment()
                    ProfileViewModel.NavigationEvent.GO_TO_MY_VEHICLES -> ProfileFragmentDirections.actionProfileFragmentToMyVehiclesFragment()
                    ProfileViewModel.NavigationEvent.GO_TO_MY_CHARGING_STATIONS -> ProfileFragmentDirections.actionProfileFragmentToMyChargingStationsFragment()
                    ProfileViewModel.NavigationEvent.GO_TO_GAMIFICATION_SETTINGS -> ProfileFragmentDirections.actionProfileFragmentToGamificationFragment()
                    ProfileViewModel.NavigationEvent.GO_TO_ACCOUNT -> ProfileFragmentDirections.actionProfileFragmentToAccountFragment()
                }
                findNavController().safeNavigate(action)
            }
        }
    }

    private fun initListeners() {
        binding?.apply {

            profilePersonalInformation.setOnClickListener {
                profileViewModel.navigateTo(ProfileViewModel.NavigationEvent.GO_TO_PERSONAL_INFORMATION)
            }

            profileMyVehicles.setOnClickListener {
                profileViewModel.navigateTo(ProfileViewModel.NavigationEvent.GO_TO_MY_VEHICLES)
            }

            profileMyStations.setOnClickListener {
                profileViewModel.navigateTo(ProfileViewModel.NavigationEvent.GO_TO_MY_CHARGING_STATIONS)
            }

            profileGamificationSettings.setOnClickListener {
                profileViewModel.navigateTo(ProfileViewModel.NavigationEvent.GO_TO_GAMIFICATION_SETTINGS)
            }

            profileAccountSettings.setOnClickListener {
                profileViewModel.navigateTo(ProfileViewModel.NavigationEvent.GO_TO_ACCOUNT)
            }

            profileLogout.setOnClickListener {
                //Do we need this line?
//                Identity.getSignInClient(requireActivity()).signOut()
                profileViewModel.logout()
            }
        }
    }
}
