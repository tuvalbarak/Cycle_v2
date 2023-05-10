package com.tdp.cycle.features.profile.my_charging_stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.customviews.CustomEmptyState
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentMyChargingStationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChargingStationsFragment :
    CycleBaseFragment<FragmentMyChargingStationsBinding>(FragmentMyChargingStationsBinding::inflate),
    CustomEmptyState.ButtonsClickListener
{

    private val myChargingStationViewModel: MyChargingStationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            myChargingStationEmptyState.setButtonsListener(this@MyChargingStationsFragment)
        }
    }

    private fun initObservers() {
        myChargingStationViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

        myChargingStationViewModel.user.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                user?.station?.let { chargingStation ->
                    myChargingStationEmptyState.gone()

                } ?: run {
                    myChargingStationEmptyState.show()

                }
            }
        }

        myChargingStationViewModel.myChargingStation.observe(viewLifecycleOwner) { chargingStation ->

        }
    }

    override fun firstButtonClick() {
        findNavController().safeNavigate(
            MyChargingStationsFragmentDirections.actionMyChargingStationFragmentToCreateChargingStationFragment()
        )
    }

}