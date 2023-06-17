package com.tdp.cycle.features.routes

import android.Manifest
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.ufrn.imd.obd.commands.ObdCommandGroup
import br.ufrn.imd.obd.commands.engine.RPMCommand
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.PolyUtil
import com.tdp.cycle.MainActivity
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.isNotNull
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.common.toLocation
import com.tdp.cycle.databinding.FragmentRoutesBinding
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.responses.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@AndroidEntryPoint
class RoutesFragment: CycleBaseFragment<FragmentRoutesBinding>(FragmentRoutesBinding::inflate), OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()

    private var googleMap: GoogleMap? = null
    private var currentLocationString: String? = null
    private val stationsMarkers = mutableListOf<Pair<Marker?, ChargingStation?>>()
    private var myMarker: Marker? = null

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    //Got back from auto-complete screen -
                    val place = Autocomplete.getPlaceFromIntent(intent)

                    Log.i("CreateChargingStationFragment", "Place: ${place.name}, ${place.id} ${place.addressComponents}")
                    place.address?.let { address ->
                        onAutoCompleteFinished(address)
                    }
//                    binding?.routesSearchEditText?.setText(place.address)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("CreateChargingStationFragment", "User canceled autocomplete")
            }
        }


//    private var mainActivity: MainActivity? by lazy { (activity as? MainActivity) }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(savedInstanceState == null) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            (childFragmentManager.findFragmentById(R.id.routesMap) as? SupportMapFragment)?.getMapAsync(this)
            initObservers()

        } else {
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Location approval needed")
//            builder.setCancelable(false)
//            builder.setPositiveButton("Approve Location") { dialog, _ ->
//                (activity as? MainActivity)?.handlePermissions()
//                dialog.dismiss()
//            }
//            builder.show()
        }

        initUi()

    }

    private fun startObdCommunication() {
        (activity as? MainActivity)?.apply {
            obdSocket?.let { socket ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (socket.isConnected) {
                        obdCommunicationFragment(socket)
                    } else {
                        connectObdSocket(socket)
                        obdCommunicationFragment(socket)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun obdCommunicationFragment(bluetoothSocket: BluetoothSocket?) {
        withContext(Dispatchers.IO) {
            bluetoothSocket?.let {
                try {
                    val inputStream = it.inputStream
                    val outputStream = it.outputStream
                    val buffer = ByteArray(1024)
                    // Group many obd commands into a single command ()
                    val obdCommands = ObdCommandGroup()
                    obdCommands.add(RPMCommand())

                    // Run all commands at once
                    var obdFetching = true
                    while(obdFetching) {
                        val response = obdCommands.run(inputStream, outputStream)
                        val response2 = obdCommands.commandPID
                        val response3 = obdCommands.result
                        val response4 = obdCommands.name

//                        mapsViewModel.rpmValue.postValue(response3.toString())



                        // Receive the response into buffer from the OBD-II device.
//                    val responseLength = inputStream.read(buffer)
//
//                    // Parse the response to extract the RPM value.
//                    val responseString = String(buffer, 0, responseLength)

                        Log.d("ressssssssponse", response.toString())
                        Log.d("ressssssssponse", response2.toString())
                        Log.d("ressssssssponse", response3.toString())
                        Log.d("ressssssssponse", response4.toString())
//                    Log.d("ressssssssponse", responseString)

                        delay(10)
                    }


                } catch (e: Exception) {
                    Log.e(RoutesFragment.TAG, "Could not connect to obd", e)
                    null
                }

            }
        }
    }


    override fun onResume() {
        super.onResume()
        mapsViewModel.getUserMe()
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

    private fun initUi() {
        binding?.apply {

            routesSearchEditText.setOnFocusChangeListener { view, isFocused ->
                if (isFocused) {
                    intentAddress()
                }
            }

            routesSearchEditText.setOnClickListener {
                intentAddress()
            }


//            routesSearchButton.setOnClickListener {
//
//                mapsViewModel.getDirections(
//                    origin = currentLocationString ?: "Ana Frank 14, Ramat-Gan",
//                    destination = routesSearchEditText.text.toString()
//                )
//
////                startObdCommunication()
//            }
            initBatteryGraph()
        }
    }

    private fun onAutoCompleteFinished(address: String) {
        mapsViewModel.getDirections(
            origin = currentLocationString ?: "Ana Frank 14, Ramat-Gan",
            destination = address
        )
    }

    private fun initBatteryGraph() {
        binding?.apply {
            routesModelBatteryGraph.gone()
            routesBatteryTitle.gone()
            routesModelBatteryGraph.max = 100
        }
    }

    private fun initObservers() {

        mapsViewModel.bestRoute.observe(viewLifecycleOwner) { mapsDirectionResponse ->
            drawBestRoute(mapsDirectionResponse)
            onRouteDrew()
        }

        mapsViewModel.geocode.observe(viewLifecycleOwner) { mapsGeocodeResults ->
            currentLocationString = mapsGeocodeResults?.firstOrNull()?.formattedAddress
        }

        mapsViewModel.chargingStations.observe(viewLifecycleOwner) { chargingStations ->
            markChargingStations(chargingStations)
        }

        mapsViewModel.elevationDifference.observe(viewLifecycleOwner) { elevation ->
            binding?.routesElevation?.text = "Elevation: " + elevation?.roundToInt().toString() + " Meters"
        }

        mapsViewModel.myEvEvent.observe(viewLifecycleOwner) { myEv ->

            binding?.apply {
                routesVehicleBrand.text = "Brand: " + myEv?.vehicleMeta?.brand
                routesVehicleModel.text = "Model: " + myEv?.vehicleMeta?.model
            }
        }

        mapsViewModel.currentUserLocation.observe(viewLifecycleOwner) {
            handleCurrentLocation(false)
        }

        mapsViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

        mapsViewModel.weather.observe(viewLifecycleOwner) { weather ->
            binding?.routesTemperature?.text = "Temperature: " + weather.current.tempC + "C"
        }

        mapsViewModel.user.observe(viewLifecycleOwner) { user ->
            mapsViewModel.getMyEv()
            binding?.routesUserName?.text = user?.let {
                "${user.name?.split(" ")?.firstOrNull()}"
            } ?: ""
        }

        mapsViewModel.batteryPercentage.observe(viewLifecycleOwner) { batteryPercentage ->
            batteryPercentage?.let {
                binding?.routesBatteryLevel?.text = "Battery: $it%"
                binding?.routesModelBatteryGraph?.visibility = View.VISIBLE
                binding?.routesBatteryTitle?.visibility = View.VISIBLE
                binding?.routesModelBatteryGraph?.progress = it.roundToInt()
            }
        }

        mapsViewModel.routeEtaAndChargingEtaEvent.observe(viewLifecycleOwner) { routeEtaAndChargingEtaEvent ->
            binding?.apply {
                routesETA.text = "ETA: ${routeEtaAndChargingEtaEvent.first}"
                routesChargingTime.isVisible = routeEtaAndChargingEtaEvent.second.isNotNull()
                routesChargingTime.text = "Charging Time: ${routeEtaAndChargingEtaEvent.second}"
            }

        }

        mapsViewModel.isObdAvailable.observe(viewLifecycleOwner) { isObdAvailable ->
            if(isObdAvailable) {
                //Nothing for now

            } else {
                //Show a dialog and ask for battery percentage
                val inputEditTextField = EditText(requireActivity())
                inputEditTextField.maxLines = 1
                inputEditTextField.inputType = InputType.TYPE_CLASS_NUMBER

                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Couldn't connect to OBD")
                    .setMessage("Enter your current EV's battery percentage...")
                    .setView(inputEditTextField)
                    .setCancelable(false)
                    .setPositiveButton("Let's start") { arg0, arg1 ->
                        val editTextInput = inputEditTextField .text.toString()
                        mapsViewModel.updateBatteryPercentage(editTextInput.toDoubleOrNull())
                    }
//                    .setPositiveButton("Let's start") { _, _ ->
//                        val editTextInput = inputEditTextField .text.toString()
//                        Log.d(TAG, "percentage => $editTextInput")
//                        mapsViewModel.updateBatteryPercentage(editTextInput.toDoubleOrNull())
//                    }
//                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                inputEditTextField.doOnTextChanged { text, start, before, count ->
                    val textAsNumber = text?.toString()?.toIntOrNull() ?: -1
                    val isValidPercentage = textAsNumber in 0..100
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isValidPercentage
                }
            }
        }
    }

    private fun markChargingStations(chargingStations: List<ChargingStation?>?) {
        chargingStations?.forEach { chargingStation ->
            val latlng = LatLng(chargingStation?.lat?.toDouble() ?: 0.0, chargingStation?.lng?.toDouble() ?: 0.0)
            val icon =
                if(chargingStation?.isPrivate == true) svgToBitmap(R.drawable.charging_station_private)
                else svgToBitmap(R.drawable.charging_station_public)

            val marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(chargingStation?.name)
//                    .snippet(chargingStation?.station_access ?: "")
                    .icon(icon)
            )
            marker?.tag = chargingStation
            stationsMarkers.add(Pair(marker, chargingStation))
        }
    }

    private fun svgToBitmap(vectorResID:Int): BitmapDescriptor? {
        ContextCompat.getDrawable(requireContext(), vectorResID)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        return null
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.setOnMarkerClickListener { marker ->
            val chargingStation = marker.tag as? ChargingStation
            findNavController().safeNavigate(
                RoutesFragmentDirections.actionRoutesFragmentToChargingStationFragment(chargingStation)
            )

            true
        }
        handleCurrentLocation(true)
    }

    private fun handleCurrentLocation(shouldMoveCamera: Boolean) {
         LocationServices.getFusedLocationProviderClient(requireActivity())
             .lastLocation
             .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Set the map's camera position to the current location of the device.
                task.result?.let { result ->
                    LatLng(result.latitude, result.longitude).let { location ->
                        mapsViewModel.saveOriginLocation(location)
                        //Getting address from latlng value
                        mapsViewModel.getGeocode(location)

                        //Getting current weather data
                        mapsViewModel.getWeather(location.toLocation())

                        if(shouldMoveCamera) {
                            //Updating camera location
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location ,12f))
                        }
                        //Adding a marker at the location (and removing previous one)
                        myMarker?.remove()
                        myMarker = googleMap?.addMarker(
                            MarkerOptions().position(location).title("Current location").icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_car)
                            )
                        )
                    }
                }
            } else {
                Log.d(TAG, "Current location is null. Using defaults.")
                Log.e(TAG, "Exception: %s", task.exception)
            }
        }
    }

    private fun drawBestRoute(route: Route?) {
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(route?.overviewPolyline?.points))
            .width(16f)
            .color(mapsViewModel.getSelectedRouteColor())

        googleMap?.addPolyline(polyline)

        route?.legs?.lastOrNull()?.let { leg ->
            leg.endLocation?.let { endLocation ->
                LatLng(endLocation.lat ?: 0.0, endLocation.lng ?: 0.0).let { latlng ->
                    googleMap?.addMarker(
                        MarkerOptions()
                            .position(latlng)
                            .title(leg.endAddress)
                    )
                }
            }
        }
        removeUnnecessaryStations()
    }

    private fun drawAllRoutes(routes: List<Route?>) {
        Log.d(TAG, "routesSize = ${routes.size}")
        routes.forEach { route ->
            val points = route?.overviewPolyline?.points
            val isRouteSelected = (route?.isRouteSelected ?: false)
            val routesColors = mapsViewModel.getRoutesColors()
            val color: Int
            val width: Float
            if(isRouteSelected) {
                color = mapsViewModel.getSelectedRouteColor()
                width = 16f
            } else {
                color = routesColors.random()
                routesColors.remove(color)
                width = 8f
            }

            val polyline = PolylineOptions()
                .addAll(PolyUtil.decode(points))
                .width(width)
                .color(color)
            googleMap?.addPolyline(polyline)

            route?.legs?.lastOrNull()?.let { leg ->
                leg.endLocation?.let { endLocation ->
                    LatLng(endLocation.lat ?: 0.0, endLocation.lng ?: 0.0).let { latlng ->
                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(latlng)
                                .title(leg.endAddress)
                        )
                    }
                }
            }
        }
        removeUnnecessaryStations()
    }

    private fun removeUnnecessaryStations() {
        //Removing all stations except the chosen one
        stationsMarkers.forEach { markerAndStation ->
            if(markerAndStation.second?.id != mapsViewModel.bestStation.value?.id) {
                markerAndStation.first?.remove()
            }
        }
    }

    private fun onRouteDrew() {
        binding?.apply {
            routesFooter.isVisible = false
        }
    }

    companion object {
        const val TAG = "RoutesFragmentTAG"
    }
}