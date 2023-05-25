package com.tdp.cycle.features.profile.my_charging_stations

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.customviews.MTPrimaryEditText
import com.tdp.cycle.databinding.FragmentCreateChargingStationBinding
import com.tdp.cycle.models.cycle_server.ChargingStationRequest
import com.tdp.cycle.models.cycle_server.ChargingStationStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChargingStationFragment : CycleBaseFragment<FragmentCreateChargingStationBinding>(FragmentCreateChargingStationBinding::inflate) {

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
            binding?.createChargingStationAddress?.setPrimaryEditTextListener(object : MTPrimaryEditText.PrimaryEditTextListener {
                override fun onPrimaryEditTextFocused(type: Int?) {
                    intentAddress()
                }
            })

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
                    binding?.createChargingStationAddress?.setText(street.toString())
                }

//                "postal_code" -> {
//                    postcode.insert(0, component.name)
//                }

//                "postal_code_suffix" -> {
//                    postcode.append("-").append(component.name)
//                }

                "locality" -> {
                    binding?.createChargingStationCity?.setText(component.name)
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

    private fun initObservers() {

    }

}