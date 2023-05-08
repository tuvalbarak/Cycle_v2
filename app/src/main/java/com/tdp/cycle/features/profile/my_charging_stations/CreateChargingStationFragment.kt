package com.tdp.cycle.features.profile.my_charging_stations

import android.os.Bundle
import android.view.View
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.databinding.FragmentCreateChargingStationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChargingStationFragment : CycleBaseFragment<FragmentCreateChargingStationBinding>(FragmentCreateChargingStationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}