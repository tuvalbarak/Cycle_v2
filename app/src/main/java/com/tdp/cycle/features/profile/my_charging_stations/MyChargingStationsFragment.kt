package com.tdp.cycle.features.profile.my_charging_stations

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.customviews.CustomEmptyState
import com.tdp.cycle.common.customviews.MTPrimaryEditText
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentMyChargingStationsBinding
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChargingStationsFragment :
    CycleBaseFragment<FragmentMyChargingStationsBinding>(FragmentMyChargingStationsBinding::inflate),
    CustomEmptyState.ButtonsClickListener
{

    private val myChargingStationViewModel: MyChargingStationViewModel by activityViewModels()

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    Log.i("CreateChargingStationFragment", "Place: ${place.name}, ${place.id} ${place.addressComponents}")
                    fillInAddress(place)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("CreateChargingStationFragment", "User canceled autocomplete")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            binding?.myChargingStationAddress?.setPrimaryEditTextListener(object : MTPrimaryEditText.PrimaryEditTextListener {
                override fun onPrimaryEditTextFocused(type: Int?) {
                    intentAddress()
                }
            })

            initSpinner()
            myChargingStationEmptyState.setButtonsListener(this@MyChargingStationsFragment)
            myChargingStationUpdate.setOnClickListener {
                intentAddress()
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

    private fun intentAddress() {
        // Initialize the SDK
        Places.initialize(requireContext(), "AIzaSyAJIBntjoplGTf0G5yqAKUr_5xbiARll4Y")

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireContext())
        startAutocomplete.launch(intent)
    }

    private fun fillInAddress(place: Place) {
        val components: AddressComponents? = place.addressComponents
        val street = StringBuilder()
        val postcode = StringBuilder()

        components?.asList()?.forEach { component ->
            when (component.types[0]) {
                "street_number" -> {
                    street.insert(0, component.name)
                }

                "route" -> {
                    street.append(" ")
                    street.append(component.shortName)
                    binding?.myChargingStationAddress?.setText(street.toString())
                }

//                "postal_code" -> {
//                    postcode.insert(0, component.name)
//                }

//                "postal_code_suffix" -> {
//                    postcode.append("-").append(component.name)
//                }

                "locality" -> {
                    binding?.myChargingStationCity?.setText(component.name)
                }

//                "administrative_area_level_1" -> {
//                    binding.autocompleteState.setText(component.shortName)
//                }

//                "country" -> {
//                    binding.autocompleteCountry.setText(component.name)
//                }
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