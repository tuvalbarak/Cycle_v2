package com.tdp.cycle.features.routes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.toLocation
import com.tdp.cycle.databinding.FragmentRoutesBinding
import com.tdp.cycle.models.ChargingStationRealModel
import com.tdp.cycle.models.StationAccess
import com.tdp.cycle.models.responses.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt
import kotlin.random.Random

@AndroidEntryPoint
class RoutesFragment: CycleBaseFragment<FragmentRoutesBinding>(FragmentRoutesBinding::inflate), OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()

    private var googleMap: GoogleMap? = null
    private var currentLocationString: String? = null
    private val stationsMarkers = mutableListOf<Pair<Marker?, ChargingStationRealModel?>>()
    private var myMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (childFragmentManager.findFragmentById(R.id.routesMap) as? SupportMapFragment)?.getMapAsync(this)

        initUi()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        mapsViewModel.getMyEv()
    }

    private fun initUi() {
        binding?.apply {
            routesSearchButton.setOnClickListener {

                mapsViewModel.getDirections(
                    origin = currentLocationString ?: "Ana Frank 14, Ramat-Gan",
                    destination = routesSearchEditText.text.toString()
                )
            }
        }
    }

    private fun initObservers() {

        mapsViewModel.bestRoute.observe(viewLifecycleOwner) { mapsDirectionResponse ->
            drawBestRoute(mapsDirectionResponse)
            onRouteDrew(mapsDirectionResponse?.legs?.firstOrNull()?.duration?.text.toString())
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
                routesVehicleBrand.text = "Brand: " + myEv?.brand
                routesVehicleModel.text = "Model: " + myEv?.model
                val image = R.drawable.ic_tesla_y
//                    if(ev?.id == 1L) R.drawable.ic_tesla_y
//                    else R.drawable.ic_mg_marvel
                routesModelImage.setImageDrawable(
                    resources.getDrawable(image, null)
                )
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
            binding?.routesUserName?.text = user?.let {
                "Hey, ${user.name?.split(" ")?.firstOrNull()}"
            } ?: ""
        }

        mapsViewModel.batteryPercentage.observe(viewLifecycleOwner) { batteryPercentage ->
            batteryPercentage?.let {
                binding?.routesBatteryLevel?.text = "Battery: $it%"
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
                    .setPositiveButton("Let's start") { _, _ ->
                        val editTextInput = inputEditTextField .text.toString()
                        Log.d(TAG, "percentage => $editTextInput")
                        mapsViewModel.updateBatteryPercentage(editTextInput.toDoubleOrNull())
                    }
//                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
        }
    }

    private fun markChargingStations(chargingStations: List<ChargingStationRealModel?>?) {
        chargingStations?.forEach { chargingStation ->
            val latlng = LatLng(chargingStation?.lat ?: 0.0, chargingStation?.lng ?: 0.0)
            val icon = when(chargingStation?.station_access) {
                StationAccess.PRIVATE.value -> svgToBitmap(R.drawable.charging_station_private)
                StationAccess.PUBLIC.value -> svgToBitmap(R.drawable.charging_station_public)
                else -> svgToBitmap(R.drawable.charging_station_public)
            }

            val marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(chargingStation?.station_name)
                    .snippet(chargingStation?.station_access ?: "")
                    .icon(icon)
            )
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
            if(markerAndStation.second?.station_id != mapsViewModel.bestStation.value?.station_id) {
                markerAndStation.first?.remove()
            }
        }
    }

    private fun onRouteDrew(ETA: String) {
        binding?.apply {
            routesETA.text = "ETA: $ETA"
            routesFooter.isVisible = false
        }
    }

    companion object {
        private const val TAG = "RoutesFragmentTAG"
    }
}