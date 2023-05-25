package com.tdp.cycle.features.routes

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.deg2rad
import com.tdp.cycle.common.rad2deg
import com.tdp.cycle.models.cycle_server.Battery
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.User
import com.tdp.cycle.models.cycle_server.VehicleMeta
import com.tdp.cycle.models.responses.*
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.ResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.*

fun Location.toLatLngFormat() = lat?.toString() + "," + lng?.toString()
fun List<Double>.average(): Double {
    return if (isEmpty()) {
        0.0
    } else {
        sum() / size
    }
}

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsRepository: MapsRepository,
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val chargingStationsRepository: ChargingStationsRepository,
    private val vehiclesRepository: VehiclesRepository
) : CycleBaseViewModel() {

    val user = MutableLiveData<User?>()
//    val routes = MutableLiveData<List<Route?>>()
    val batteryPercentage = MutableLiveData<Double>()
    val bestRoute = MutableLiveData<Route>()
    val geocode = MutableLiveData<List<MapsGeocodeResults?>?>()
    val elevationDifference = MutableLiveData<Double?>()
    val chargingStations = MutableLiveData<List<ChargingStation?>?>()
    val bestStation = MutableLiveData<ChargingStation?>()
//    val electricVehiclesEvent = MutableLiveData<List<ElectricVehicleModel>>()
    val myEvEvent = MutableLiveData<ElectricVehicle?>()
    val currentUserLocation = MutableLiveData(false)
    val weather = MutableLiveData<WeatherResponse>()
    val isObdAvailable = MutableLiveData<Boolean>()
    private var isInRoute = false
    private var originLocation: LatLng? = null

    val rpmValue: MutableLiveData<String> = MutableLiveData("N/A")


    init {

        safeViewModelScopeIO {
            progressData.startProgress()
            when(val response = chargingStationsRepository.getChargingStations()) {
                is RemoteResponseSuccess -> chargingStations.postValue(response.data)
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
//            chargingStationsRepositoryDepricated.fetchChargingStationsLocations()
            updateCurrentLocation()

            //Currently no obd integration -> always false.
            isObdAvailable.postValue(false)

            progressData.endProgress()
        }
    }

    fun getUserMe() {
        safeViewModelScopeIO {
            progressData.startProgress()
            when(val response = userRepository.getUserMe(fetchFromServer = true)) {
                is RemoteResponseSuccess -> {
                    user.postValue(response.data)
                }
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
            progressData.endProgress()
        }
    }


    fun calculateRadiansBetweenTwoPoints(start: Location?, end: Location?): Double {
        val lat1 = Math.toRadians(start?.lat ?: 0.0)
        val lon1 = Math.toRadians(start?.lng ?: 0.0)
        val lat2 = Math.toRadians(end?.lat ?: 0.0)
        val lon2 = Math.toRadians(end?.lng ?: 0.0)
        val deltaLon = lon2 - lon1
        val y = sin(deltaLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLon)
        val bearing = atan2(y, x)
        return if (bearing < 0) bearing + 2 * Math.PI else bearing
    }

    private fun calculateWindEffect(drivingDirectionRadians: Double, windDirectionRadians: Double, windSpeed: Double): Double {
        val difference = windDirectionRadians - drivingDirectionRadians
        val cosineDifference = cos(difference)
        val percentageChange = (cosineDifference * windSpeed * 0.3) / 100
        return 1 - percentageChange
    }

    fun getMyEv() {

        safeViewModelScopeIO {
            progressData.startProgress()
            user.value?.myElectricVehicle?.let {
                val myEv = (vehiclesRepository.getElectricVehiclesById(it) as? RemoteResponseSuccess)?.data
                myEvEvent.postValue(myEv)
            }
            progressData.endProgress()
//            myEvEvent.postValue(userRepositoryDepricated.getLastSelectedEV())
        }
    }

    fun updateBatteryPercentage(percentage: Double?) {
        batteryPercentage.postValue(percentage ?: 0.0)
    }

//    fun calculateTemperatureEffect(temperature: Double): Double {
//        val baseTemperature = 22.0
//        val difference = abs(temperature - baseTemperature)
//        return 1 + difference * 0.005
//    }
//
//    fun calculateHumidityEffect(humidity: Double): Double {
//        val baseHumidity = 50.0
//        val difference = abs(humidity - baseHumidity)
//        return 1 + difference * 0.002
//    }

    fun calculateTemperatureEffect(temperature: Double): Double {
        val baseTemperature = 22f
        val distance = abs(temperature - baseTemperature)
        val exponent = distance * 0.05 //changes how big the difference from base
        return 1 + (1.0 - exp(-exponent)) / 10 //changes the final value
    }

    fun calculateHumidityEffect(humidity: Double): Double {
        val baseHumidity = 50
        val distance = abs(humidity - baseHumidity)
        val exponent = distance * 0.05 //changes how big the difference from base
        return 1 + (1.0 - exp(-exponent)) / 20 //changes the final value
    }


    fun calculateElevationEffect(elevation: Double): Double {
        val baseElevation = 0
        val distance = elevation - baseElevation
        val isNegative = distance < 0
        val exponent = abs(distance) * 0.01
        return if(isNegative) 1 - (1.0 - exp(-exponent)) / 10
            else 1 + (1.0 - exp(-exponent)) / 10
    }

//    fun calculateEnergyConsumption(baseConsumption: Double, humidity: Double, temperature: Double): Double {
//        val humidityFactor = 1.0 + (humidity - 40.0) * (0.0095 + 0.0018 * (temperature - 22.0))
//        val temperatureFactor = 1.0 + 0.0026 * (temperature - 22.0) + 0.00036 * Math.pow((temperature - 22.0), 2.0)
//        val adjustedConsumption = baseConsumption * humidityFactor * temperatureFactor
//        return adjustedConsumption
//    }
    fun saveOriginLocation(origin: LatLng) {
        originLocation = origin
    }

    fun getWeather(location: Location) {
        safeViewModelScopeIO {
//            progressData.startProgress()
            val weatherResponse = weatherRepository.getWeather(location)
            if (weatherResponse.isSuccessful) {
//                Log.d(TAG, weatherResponse.body().toString())
                weather.postValue(weatherResponse.body())
            }
//            progressData.endProgress()
        }
    }

    /**
     * If the user is currently on route --> update his location on the map every X seconds.
     * */
    private fun updateCurrentLocation() {
//        safeViewModelScopeIO {
//            while(true) {
//                delay(10000)
//                if(isInRoute) {
//                    currentUserLocation.postValue(true)
//                }
//            }
//        }
    }

    fun getDirections(origin: String, destination: String) {
        isInRoute = true
        safeViewModelScopeIO {
            progressData.startProgress()

            //Routes received from Google
            val directionsResponse = mapsRepository.getDirections(origin = origin, destination = destination)

            if (directionsResponse.isSuccessful) {
                val originLatLng = directionsResponse.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.startLocation
                val destinationLatLng = directionsResponse.body()?.routes?.firstOrNull()?.legs?.lastOrNull()?.endLocation

                calculateElevationDifference(originLatLng, destinationLatLng)

                val recommendedRoute = getBestRouteAccordingToGoogle(directionsResponse.body()?.routes)
                val bestStation = recommendedRoute?.let {
                    user.value?.myElectricVehicle?.let {
                        val currentEv = (vehiclesRepository.getElectricVehiclesById(it) as? RemoteResponseSuccess)?.data
                        getBestStation(recommendedRoute, currentEv?.battery)
                    }
                }

                bestStation?.let { station ->
                    //Need to stop
                    val waypoints = station.getLocation().toLatLngFormat()
                    val modifiedDirectionsResponse = mapsRepository.getDirections(
                        origin = origin,
                        destination = destination,
                        waypoints = "via:${waypoints}"
                    )

                    if (modifiedDirectionsResponse.isSuccessful) {
                        getBestRouteAccordingToGoogle(modifiedDirectionsResponse.body()?.routes)?.let { route ->
                            bestRoute.postValue(route)
                        }
                    }

                } ?: run {
//                    Don't need to stop
                    recommendedRoute?.let {
                        bestRoute.postValue(it)
                    }
                }
            }
            progressData.endProgress()
        }

    }

    //Tesla Model Y:
    // WH/KM - 167
    // Range(KM) - 345
    // Capacity - 57,500
    // Current percentage - 15%
    private fun getBestStation(route: Route, battery: Battery?): ChargingStation? {
        val currentBatteryConsumption = battery?.consumptionPerKm ?: 0
        val drivingDirection = calculateRadiansBetweenTwoPoints(route.legs?.firstOrNull()?.startLocation, route.legs?.lastOrNull()?.endLocation)
        val windEffect = calculateWindEffect(drivingDirection, weather.value?.current?.windDegree ?: 0.0, weather.value?.current?.windKph ?: 0.0)
        val temperatureEffect = calculateTemperatureEffect(weather.value?.current?.tempC ?: 22.0)
        val humidityEffect = calculateHumidityEffect(weather.value?.current?.humidityPercentage ?: 50.0)

        val elevationEffect = calculateElevationEffect(elevationDifference.value ?: 0.0)
        Log.d(TAG, "elevationDifference => $elevationEffect")

        val actualBatteryConsumption = currentBatteryConsumption * windEffect * temperatureEffect * elevationEffect * humidityEffect//Battery consumption considering different params
        val currentBatteryPercentage = (batteryPercentage.value ?: 0.0) / 100
        val batteryMaxCapacity = battery?.batteryCapacity ?: 0
        val batteryMistakeDelta = 0.8 //reducing 20% extra as precaution
        val currentBatteryRange = ((currentBatteryPercentage * batteryMaxCapacity) / actualBatteryConsumption) * batteryMistakeDelta
        var routeLengthMeters = 0
        route.legs?.forEach {
            routeLengthMeters += (it?.distance?.value ?: 0)
        }
        val routeLengthKM = routeLengthMeters / 1000

        Log.d(TAG, "currentBatteryRange = $currentBatteryRange, routeLengthKM = $routeLengthKM")

        return if (currentBatteryRange > routeLengthKM) {
            //Can do entire route without stopping
            null
        } else {
            //Have to stop
            findStationWithinKm(currentBatteryRange, route)
        }
    }

    data class RouteStationDistance(
        var distanceFromRoute: Double,
        var distanceFromOrigin: Double,
        var station: ChargingStation
    )

    private fun findStationWithinKm(mustStopBeforeKM: Double, route: Route): ChargingStation? {
        var minDistanceBetweenStepAndStation = Double.MAX_VALUE
        var currentBestStation: ChargingStation? = null
        val distances = mutableListOf<RouteStationDistance>()

        // Going through all legs (not sure - think Leg is a path along the route - may be relevant if we have stops in our root)
        route.legs?.forEach { leg ->
            // Steps is all the turnovers we need to make along the route. e.g., פנה ימינה לרחוב המרי
            leg?.steps?.forEach { step ->
                // Now crossing between all possible steps (from all possible routes & legs) and our charging stations
                chargingStations.value?.forEach { chargingStation ->
                    val arePrivateStationsAllowed = user.value?.userPreferance?.arePrivateChargingStationsAllowed ?: false
                    if(chargingStation?.isPrivate == true || !arePrivateStationsAllowed) {

                        // Making sure nothing is null
                        chargingStation?.lat?.let { chargingStationLat ->
                            chargingStation.lng?.let { chargingStationLng ->
                                step?.endLocation?.lat?.let { stepLat ->
                                    step.endLocation?.lng?.let { stepLng ->
                                        originLocation?.let { origin ->

                                            // Getting current distance from stop to charging station
                                            val currentDistanceBetweenStepAndStation =
                                                calculateDistance(
                                                    chargingStationLat,
                                                    chargingStationLng,
                                                    stepLat.toFloat(),
                                                    stepLng.toFloat()
                                                )
                                            val distanceFromOrigin =
                                                calculateDistance(
                                                    chargingStationLat,
                                                    chargingStationLng,
                                                    origin.latitude.toFloat(),
                                                    origin.longitude.toFloat()
                                                )

                                            distances.add(
                                                RouteStationDistance(
                                                    currentDistanceBetweenStepAndStation.toDouble(),
                                                    distanceFromOrigin.toDouble(),
                                                    chargingStation
                                                )

                                            )

                                            // If we found better station
                                            if (currentDistanceBetweenStepAndStation < minDistanceBetweenStepAndStation) {
                                                // Saving new params
                                                minDistanceBetweenStepAndStation = currentDistanceBetweenStepAndStation.toDouble()
                                                currentBestStation = chargingStation
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        val sortedDistances = distances.sortedBy { it.distanceFromRoute }

        route.isRouteSelected = true

        currentBestStation?.distanceFromRoute = sortedDistances.firstOrNull()?.distanceFromRoute

        //Need to go through all distances (the sorted list)
        // each distance - if the originDistance is bigger than max - null. else - lets fucking go

        //If found a station that we can stop before running out of power:
        sortedDistances.forEach { routeStationDistance ->
            if(routeStationDistance.distanceFromOrigin < mustStopBeforeKM) {
                return handleFoundBestStation(routeStationDistance.station)
            }
        }
        //If couldn't found:
        return null

        //Need to go through all stations
        //  each station - save its distance from origin
        //  find closest station to origin
//        var closestStationToOriginDistance: Double = Double.MAX_VALUE
//        var closestStationToOrigin: ChargingStationRealModel? = null
//        originLocation?.let { origin ->
//            chargingStations.value?.forEach { chargingStation ->
//                // Making sure nothing is null
//                chargingStation?.lat?.let { chargingStationLat ->
//                    chargingStation.lng?.let { chargingStationLng ->
//                        val distanceBetweenOriginAndStation =
//                            calculateDistance(
//                                chargingStationLat,
//                                chargingStationLng,
//                                origin.latitude,
//                                origin.longitude
//                        )
//                        if(closestStationToOriginDistance > distanceBetweenOriginAndStation) {
//                            closestStationToOriginDistance = distanceBetweenOriginAndStation
//                            closestStationToOrigin = chargingStation
//                        }
//                    }
//                }
//            }
//        }


        //

//        if(mustStopBeforeKM < (currentBestStation?.distanceFromRoute ?: Double.MAX_VALUE)) {
//            //Can't stop at the best station - trying to stop at closest to origin
//            return if(mustStopBeforeKM < closestStationToOriginDistance) {
//                //Cant complete route - battery is insufficient
//                Log.d(TAG, "Cant complete route - battery is insufficient")
//                null
//            } else {
//                //Stopping at closest station to origin
//                handleFoundBestStation(closestStationToOrigin)
//            }
//
//
//        } else {
//            //Route is OK
//            return handleFoundBestStation(currentBestStation)
//        }
    }

    private fun handleFoundBestStation(best: ChargingStation?): ChargingStation? {
        bestStation.postValue(best)

        val currentChargingStations = chargingStations.value?.toMutableList()
        currentChargingStations?.find { it?.id == best?.id }?.let {
            currentChargingStations.remove(best)
            best?.distanceFromRoute = best?.distanceFromRoute
            currentChargingStations.add(best)
            chargingStations.postValue(currentChargingStations)

        }
        return best
    }

    private fun calculateElevationDifference(origin: Location?, destination: Location?) {
        safeViewModelScopeIO {
            progressData.startProgress()
            val originElevationResponse = mapsRepository.getElevation(
                origin?.lat.toString() + "," + origin?.lng.toString()
            )

            val destinationElevationResponse = mapsRepository.getElevation(
                destination?.lat.toString() + "," + destination?.lng.toString()
            )

            val difference = destinationElevationResponse.body()?.mapsGeocodeResults?.firstOrNull()?.elevation?.toDoubleOrNull()?.minus(
                originElevationResponse.body()?.mapsGeocodeResults?.firstOrNull()?.elevation?.toDoubleOrNull() ?: 0.0
            ) ?: 0.0

            elevationDifference.postValue(difference)
            progressData.endProgress()
        }
    }

    fun getGeocode(latLng: LatLng) {
        safeViewModelScopeIO {
//            progressData.startProgress()
            val res = latLng.latitude.toString() + "," + latLng.longitude.toString()
            val response = mapsRepository.getGeocodeByLatLng(latLng = res,)
            if(response.isSuccessful) {
                geocode.postValue(response.body()?.mapsGeocodeResults)
            }
//            progressData.endProgress()
        }
    }

    /**
    * Calculates the effect of wind on battery consumption in kWh/km.
    *
    * @param windDirection The wind direction in degrees (0-360).
    * @param windSpeed The wind speed in m/s.
    * @param vehicleSpeed The vehicle speed in km/h.
    * @param vehicleFrontalArea The frontal area of the vehicle in m^2.
    * @param vehicleDragCoefficient The drag coefficient of the vehicle.
    * @param vehicleMass The mass of the vehicle in kg.
    * @return The effect of wind on battery consumption in kWh/km.
    */
    private fun calculateWindEffect(
        windDirection: Double,
        windSpeed: Double,
        vehicleSpeed: Double,
        vehicleFrontalArea: Double,
        vehicleDragCoefficient: Double,
        vehicleMass: Double
    ): Double {
        val windAngle = abs(windDirection - 180) % 180 // angle between wind and vehicle direction
        val windForce = 0.5 * 1.23 * (windSpeed * vehicleSpeed) * vehicleFrontalArea // N
        val windPower = windForce * vehicleSpeed // W
        val airDensity = 1.23 // kg/m^3
        val airViscosity = 1.8E-5 // kg/m-s
        val reynoldsNumber = (airDensity * vehicleSpeed * vehicleFrontalArea) / airViscosity
        val dragCoefficient = vehicleDragCoefficient + (1.5 / (reynoldsNumber * vehicleFrontalArea)) // includes turbulence correction
        val dragForce = 0.5 * airDensity * (vehicleSpeed * vehicleSpeed) * dragCoefficient * vehicleFrontalArea // N
        val dragPower = dragForce * vehicleSpeed // W
        val totalPower = dragPower + windPower // W
        val totalEnergy = totalPower * 3600.0 // J/km
        val energyPerKm = totalEnergy / 1000.0 // J/m to kWh/m
        return energyPerKm / (vehicleMass * 9.81) // kWh/km per unit of mass
    }

    /**
     * Calculates the effect of direction on battery consumption in kWh/km.
     *
     * @param routeDirection The direction of the route.
     * @param vehicleDirection The direction the vehicle is facing.
     * @return The effect of direction on battery consumption in kWh/km.
     */
    fun calculateDirectionEffect(
        routeDirection: Direction,
        vehicleDirection: Direction
    ): Double {
        val angle = Math.abs(vehicleDirection.degrees - routeDirection.degrees)
        return when {
            angle < 45 -> 0.0
            angle < 135 -> 0.02
            angle < 180 -> 0.05
            else -> 0.1
        }
    }
    enum class Direction(val degrees: Int) {
        NORTH(0),
        EAST(90),
        SOUTH(180),
        WEST(270)
    }

    //I want the sum of all parameters' sum to be 100.
    //Calculating each param, while doing so --> giving each station the relevant score(for this param)
    //After finished calculating all params - sorting stations
    //First station is the best according to given param and weights

    //Maybe to make it easier for Alpha - each parameter will have exactly one point --> the route with most votes will win
    // Battery sufficiency should overrule all the others

    //100
    // Closest station - 50
    //
    //we
    //Also need to include - Temperature, elevation, battery attributes (percentage, projected percentage along the route),
    // wind, driver preferences, driver style, and fucking more.

    //If battery is not sufficient
    //    find out out many kms it has, reduce some delta (i.e., 20%) - this will be the upper bound of station distance from origin
    //else
    //    we can use upper bound to be equal to the entire trip distance
    //

    //We get 3 routes: R1, R2, R3.
    //each Route will have the following diferences:
    // Elevation differences in calculated from each leg
    // Are user's preferences satisfied
    //

    //each Route will have the following same:
    // Initial battery
    // Battery status along the route
    //


    //Route has been chosen --> Current Battery supposed to last 100 KMs, route is 140 KMs.
    // Average battery consumption, driver characteristics (speed average, brakes usages per minute),
    // Temperature, Elevation, Wind,

    //      Closest to path
    // A         1
    // B         3
    // C         2


    //Tesla Model Y:
        // Wh/KM - 167
        // Range(KM) - 345
        // Capacity - 57,500
        // Current percentage - 100%

    private fun getEstimatedKWH(): Double {


        val batteryAverageConsumption = 167

        val averageDriverSpeed: Double? = null
        val averageDriverBreaks: Double? = null
        /*****************************/
//        val windDirection = weather.value?.current?.windDirection
//        val windKph = weather.value?.current?.windKph
        //check where in map
//        bestRouteAccordingGoogle?.legs?.firstOrNull()?.startLocation
        //check where in map
//        bestRouteAccordingGoogle?.legs?.lastOrNull()?.endLocation
        //return is Pair<Direction, Direction>
        /*****************************/

        // Scale the input values to have similar ranges
//        val speedScaled = (averageSpeed - 50) / 50 // Assumes a typical speed range of 0-100 km/h
//        val breakScaled = averageUseOfBreak / 100 // Assumes a typical break usage range of 0-100%
        val elevationScaled = elevationDifference.value?.div(100) // Assumes a typical elevation range of -100 to 100 meters
        val temperatureScaled = (weather.value?.current?.tempC?.minus(20))?.div(30) // Assumes a typical temperature range of 0-50 degrees Celsius
//        val windScaled = wind / 10 // Assumes a typical wind range of 0-10 m/s
        // Calculate weights based on the scaled values
//        val speedWeight = 1.0
//        val breakWeight = 1.0
        val elevationWeight = 1.0 - abs(elevationScaled ?: 0.0)
        val temperatureWeight = 1.0 - abs(temperatureScaled ?: 0.0)
//        val windWeight = 1.0 - abs(windScaled)
        // Calculate the estimated battery usage based on the input variables and their weights
        val batteryUsage = batteryAverageConsumption * (
//                (averageSpeed +
                (elevationDifference.value?.div(10))?.plus((weather.value?.current?.tempC?.minus(20))?.div(50) ?: 0.0) ?: 0.0
//                        (averageUseOfBreak / 100) +

//                        wind / 50
                )

        Log.d(TAG, "batteryUsage = $batteryUsage")

        // Multiply the estimated battery usage by the weights of each variable
        return batteryUsage * (
//                speedWeight +
//                        breakWeight +
                        elevationWeight +
                        temperatureWeight
//                        windWeight
                )

    }

    fun evaluateBatteryUsage(
        batteryCapacity: Double,
        batteryAverageConsumption: Double,
        averageSpeed: Double,
        averageUseOfBreak: Double,
        routeElevationDifference: Double?,
        routeTemperature: Double?,
        routeWind: Double?
    ): Double {
        // Check if routeElevationDifference is null and set a default value
        val elevationDiff = routeElevationDifference ?: 0.0
        // Check if routeTemperature is null and set a default value
        val temperature = routeTemperature ?: 20.0 // Assuming the default temperature is 20 degrees Celsius
        // Check if routeWind is null and set a default value
        val wind = routeWind ?: 0.0 // Assuming there is no wind by default
        // Scale the input values to have similar ranges
        val speedScaled = (averageSpeed - 50) / 50 // Assumes a typical speed range of 0-100 km/h
        val breakScaled = averageUseOfBreak / 100 // Assumes a typical break usage range of 0-100%
        val elevationScaled = elevationDiff / 100 // Assumes a typical elevation range of -100 to 100 meters
        val temperatureScaled = (temperature - 20) / 30 // Assumes a typical temperature range of 0-50 degrees Celsius
        val windScaled = wind / 10 // Assumes a typical wind range of 0-10 m/s
        // Calculate weights based on the scaled values
        val speedWeight = 1.0
        val breakWeight = 1.0
        val elevationWeight = 1.0 - abs(elevationScaled)
        val temperatureWeight = 1.0 - abs(temperatureScaled)
        val windWeight = 1.0 - abs(windScaled)
        // Calculate the estimated battery usage based on the input variables and their weights
        val batteryUsage = batteryCapacity * batteryAverageConsumption * (
                (averageSpeed + (elevationDiff / 10)) / 100 +
                        (averageUseOfBreak / 100) +
                        (temperature - 20) / 50 +
                        wind / 50
                )
        // Multiply the estimated battery usage by the weights of each variable
        return batteryUsage * (
                speedWeight +
                        breakWeight +
                        elevationWeight +
                        temperatureWeight +
                        windWeight
                )
    }


    private fun getBestRouteAccordingToGoogle(routes: List<Route?>?) =
        routes?.sortedBy { it?.legs?.firstOrNull()?.duration?.value }?.firstOrNull()

    private fun findClosestChargingStationToRoute(routes: List<Route?>?): List<Route?>? {

        var minDistanceBetweenStepAndStation = Double.MAX_VALUE
        var currentBestStation: ChargingStation? = null
        var currentBestRoute: Route? = null
        //distances list is just for debugging:
        val distances = mutableSetOf<Pair<Double, String?>>()

        //Going through all routes
        routes?.forEach { route ->
            // Going through all legs (not sure - think Leg is a path along the route - may be relevant if we have stops in our root)
            route?.legs?.forEach { leg ->
                // Steps is all the turnovers we need to make along the route. e.g., פנה ימינה לרחוב המרי
                leg?.steps?.forEach { step ->
                    // Now crossing between all possible steps (from all possible routes & legs) and our charging stations
                    chargingStations.value?.forEach { chargingStation ->
                        // Making sure nothing is null
                        chargingStation?.lat?.let { chargingStationLat ->
                            chargingStation.lng?.let { chargingStationLng ->
                                step?.endLocation?.lat?.let { stepLat ->
                                    step.endLocation?.lng?.let { stepLng ->

                                        // Getting current distant from stop to charging station
                                        val currentDistanceBetweenStepAndStation = calculateDistance(
                                            chargingStationLat,
                                            chargingStationLng,
                                            stepLat.toFloat(),
                                            stepLng.toFloat()
                                        )
                                        // Saving all distances just for debugging
                                        distances.add(Pair(currentDistanceBetweenStepAndStation.toDouble(), chargingStation.name))

                                        // If we found better station
                                        if(currentDistanceBetweenStepAndStation < minDistanceBetweenStepAndStation) {
                                            // Saving new params
                                            minDistanceBetweenStepAndStation = currentDistanceBetweenStepAndStation.toDouble()
                                            currentBestStation = chargingStation
                                            currentBestRoute = route
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        val sortedDistances = distances.sortedBy { it.first }

        // Going through all routes, isRouteSelected will be true only in the best route (so UI layer can decide what to do with it)
        routes?.forEach { route ->
            route?.isRouteSelected = (route == currentBestRoute)
        }

        currentBestStation?.distanceFromRoute = sortedDistances.firstOrNull()?.first
        bestStation.postValue(currentBestStation)

        val currentChargingStations = chargingStations.value?.toMutableList()
        currentChargingStations?.find { it?.id == currentBestStation?.id }?.let { best ->
            currentChargingStations.remove(best)
            best.distanceFromRoute = currentBestStation?.distanceFromRoute
            currentChargingStations.add(best)
            chargingStations.postValue(currentChargingStations)
        }

        return routes
    }

    // Returns the distance in KMs between 2 points
    private fun calculateDistance(lat1: Float, lon1: Float, lat2: Float, lon2: Float): Float {
        val theta = lon1 - lon2
        var dist = sin(lat1.deg2rad()) * sin(lat2.deg2rad()) + cos(lat1.deg2rad()) * cos(lat2.deg2rad()) * cos(theta.deg2rad())
        dist = acos(dist)
        dist = dist.rad2deg()
        dist *= 60f * 1.1515f * 1.609344f
        return dist
    }

    fun getRoutesColors() = mutableListOf(
        Color.argb(255, 200, 200, 200),
        Color.argb(255, 100, 100, 100),
        Color.argb(255, 0, 0, 0),
        Color.argb(255, 42, 123, 241),
        Color.argb(255, 58, 22, 188),
        Color.argb(255, 222, 11, 223),
        Color.argb(255, 105, 150, 30),
        Color.argb(255, 180, 44, 10),
    )

    fun getSelectedRouteColor() = Color.argb(255, 48, 250, 2)


    companion object {
        private const val TAG = "MainViewModelTAG"
    }

}