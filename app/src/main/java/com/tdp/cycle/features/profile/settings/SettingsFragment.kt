package com.tdp.cycle.features.profile.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : CycleBaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {

        binding?.apply {

            settingsAllowTollSwitch.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.updatePushAllowTollRoadsInSP(isChecked)
            }

            settingsMultipleChargingSwitch.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.updateMultipleChargingStopsInSP(isChecked)
            }

            settingsNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.updatePushNotificationsInSP(isChecked)
            }
        }
    }

    private fun initObservers() {

        settingsViewModel.allowPushNotifications.observe(viewLifecycleOwner) { isChecked ->
            binding?.settingsNotificationsSwitch?.isChecked = isChecked
        }

        settingsViewModel.allowTollRoads.observe(viewLifecycleOwner) { isChecked ->
            binding?.settingsAllowTollSwitch?.isChecked = isChecked
        }

        settingsViewModel.allowMultipleChargingStops.observe(viewLifecycleOwner) { isChecked ->
            binding?.settingsMultipleChargingSwitch?.isChecked = isChecked
        }
    }

}