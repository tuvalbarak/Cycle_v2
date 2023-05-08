package com.tdp.cycle.features.profile.my_charging_stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentMyChargingStationsBinding

class MyChargingStationsFragment : CycleBaseFragment<FragmentMyChargingStationsBinding>(FragmentMyChargingStationsBinding::inflate) {

    private val myChargingStationViewModel: MyChargingStationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            myChargingStationsFab.setOnClickListener {
                findNavController().safeNavigate(
                    MyChargingStationsFragmentDirections.actionMyChargingStationFragmentToCreateChargingStationFragment()
                )
            }
        }
    }

    private fun initObservers() {
        myChargingStationViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

        myChargingStationViewModel.user.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                user?.station?.let { chargingStation ->
                    myChargingStationsFab.gone()

                } ?: run {
                    myChargingStationsFab.show()

                }
            }
        }

        myChargingStationViewModel.myChargingStation.observe(viewLifecycleOwner) { chargingStation ->

        }
    }

}