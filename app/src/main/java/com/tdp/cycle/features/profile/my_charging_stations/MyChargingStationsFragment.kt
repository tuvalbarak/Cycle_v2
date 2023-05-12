package com.tdp.cycle.features.profile.my_charging_stations

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.customviews.CustomEmptyState
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentMyChargingStationsBinding
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import com.tdp.cycle.models.cycle_server.ChargingStationStatus
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
            initSpinner()
            myChargingStationEmptyState.setButtonsListener(this@MyChargingStationsFragment)
            myChargingStationUpdate.setOnClickListener {
                val chargingStationRequest = ChargingStationRequest(
                    name = myChargingStationName.getText(),
                    lat = 0f,
                    lng = 0f,
                    priceDetails = myChargingStationPrice.getText(),
                    address = myChargingStationAddress.getText(),
                    city = myChargingStationCity.getText(),
                    count = 1f,
                    power = myChargingStationPower.getText().toFloatOrNull() ?: 0f,
                    connectorType = myChargingStationConnectorType.getText(),
                    condition = myChargingStationStatusSpinner.selectedItem.toString(),
                    isPrivate = true,
                    ownerId = 0
                )
                myChargingStationViewModel.updateChargingStation(
                    chargingStationRequest
                )
            }
        }
    }

//    private var isSpinnerInitialized = false

    private fun initSpinner() {
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.charging_station_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding?.myChargingStationStatusSpinner?.adapter = adapter
            binding?.myChargingStationStatusSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
//                    if(isSpinnerInitialized) {
//                        val status = when(selectedItem) {
//                            ChargingStationStatus.AVAILABLE.ordinal -> ChargingStationStatus.AVAILABLE.value
//                            ChargingStationStatus.BROKEN.ordinal -> ChargingStationStatus.BROKEN.value
//                            else -> ChargingStationStatus.OCCUPIED.value
//                        }
//                        chargingStationViewModel.onChargingStationStatusChanged(status)
//                    }
//                    isSpinnerInitialized = true

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
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
                    setMyChargingStationDetails(chargingStation)
                    myChargingStationEmptyState.gone()
                    myChargingStationLayout.show()
                    myChargingStationUpdate.show()
                } ?: run {
                    myChargingStationEmptyState.show()
                    myChargingStationLayout.gone()
                    myChargingStationUpdate.gone()
                }
            }
        }

        myChargingStationViewModel.myChargingStation.observe(viewLifecycleOwner) { chargingStation ->
            setMyChargingStationDetails(chargingStation)
        }

        myChargingStationViewModel.chargingStationUpdated.observe(viewLifecycleOwner) { isUpdated ->
            binding?.apply {
                if(isUpdated) {
                    Snackbar.make(root, "Station was updated successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMyChargingStationDetails(chargingStation: ChargingStation?) {
        binding?.apply {
            chargingStation?.let {
                myChargingStationEmptyState.gone()
                myChargingStationLayout.show()
                myChargingStationUpdate.show()

                myChargingStationConnectorType.setText(chargingStation.connectorType)
                myChargingStationPower.setText(chargingStation.power.toString())
                myChargingStationPrice.setText(chargingStation.priceDetails)
                myChargingStationCity.setText(chargingStation.city)
                myChargingStationAddress.setText(chargingStation.address)
                myChargingStationName.setText(chargingStation.name)

            } ?: run {
                myChargingStationEmptyState.show()
                myChargingStationLayout.gone()
                myChargingStationUpdate.gone()
            }
        }
    }

    override fun firstButtonClick() {
        findNavController().safeNavigate(
            MyChargingStationsFragmentDirections.actionMyChargingStationFragmentToCreateChargingStationFragment()
        )
    }

}