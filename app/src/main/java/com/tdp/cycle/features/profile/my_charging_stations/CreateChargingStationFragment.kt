package com.tdp.cycle.features.profile.my_charging_stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.databinding.FragmentCreateChargingStationBinding
import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import com.tdp.cycle.models.cycle_server.ChargingStationStatus
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.checkOffsetAndCount

@AndroidEntryPoint
class CreateChargingStationFragment : CycleBaseFragment<FragmentCreateChargingStationBinding>(FragmentCreateChargingStationBinding::inflate) {

    private val myChargingStationViewModel: MyChargingStationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            createChargingStationSave.setOnClickListener {
                val chargingStationRequest = ChargingStationRequest(
                    name = createChargingStationName.getText(),
                    lat = 0f,
                    lng = 0f,
                    priceDetails = createChargingStationPrice.getText(),
                    address = createChargingStationAddress.getText(),
                    city = createChargingStationCity.getText(),
                    count = 1f,
                    power = createChargingStationPower.getText().toFloatOrNull() ?: 0f,
                    connectorType = createChargingStationConnectorType.getText(),
                    condition = ChargingStationStatus.AVAILABLE.value,
                    isPrivate = true,
                    ownerId = 0
                )
                myChargingStationViewModel.createChargingStation(chargingStationRequest)
            }
        }
    }

    private fun initObservers() {

    }

}