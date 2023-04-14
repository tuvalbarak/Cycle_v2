package com.tdp.cycle.models.responses

import com.google.gson.annotations.SerializedName

/************************************* Maps Directions ***********************************/
data class MapsDirectionsResponse(
    @SerializedName("geocoded_waypoints") var geocodedWaypoints: List<GeocodedWaypoint?>?,
    @SerializedName("routes") var routes: List<Route?>?,
    @SerializedName("status") var status: String?
)

data class Route(
    @SerializedName("bounds") var bounds: Bounds?,
    @SerializedName("copyrights") var copyrights: String?,
    @SerializedName("legs") var legs: List<Leg?>?,
    @SerializedName("overview_polyline") var overviewPolyline: OverviewPolyline?,
    @SerializedName("summary") var summary: String?,
    @SerializedName("warnings") var warnings: List<Any?>?,
    @SerializedName("waypoint_order") var waypointOrder: List<Any?>?,
    var isRouteSelected: Boolean?
)

data class GeocodedWaypoint(
    @SerializedName("geocoder_status") var geocoderStatus: String?,
    @SerializedName("place_id") var placeId: String?,
    @SerializedName("types") var types: List<String?>?
)

data class Leg(
    @SerializedName("distance") var distance: Distance?,
    @SerializedName("duration") var duration: Duration?,
    @SerializedName("end_address") var endAddress: String?,
    @SerializedName("end_location") var endLocation: Location?,
    @SerializedName("start_address") var startAddress: String?,
    @SerializedName("start_location") var startLocation: Location?,
    @SerializedName("steps") var steps: List<Step?>?,
    @SerializedName("traffic_speed_entry") var trafficSpeedEntry: List<Any?>?,
    @SerializedName("via_waypoint") var viaWaypoint: List<Any?>?
)

data class Bounds(
    @SerializedName("northeast") var northeast: Location?,
    @SerializedName("southwest") var southwest: Location?
)

data class Distance(
    @SerializedName("text") var text: String?,
    @SerializedName("value") var value: Int?
)

data class Duration(
    @SerializedName("text") var text: String?,
    @SerializedName("value") var value: Int?
)

data class OverviewPolyline(
    @SerializedName("points") var points: String?
)

data class Polyline(
    @SerializedName("points") var points: String?
)

data class Step(
    @SerializedName("distance") var distance: Distance?,
    @SerializedName("duration") var duration: Duration?,
    @SerializedName("end_location") var endLocation: Location?,
    @SerializedName("html_instructions") var htmlInstructions: String?,
    @SerializedName("maneuver") var maneuver: String?,
    @SerializedName("polyline") var polyline: Polyline?,
    @SerializedName("start_location") var startLocation: Location?,
    @SerializedName("travel_mode") var travelMode: String?
)

/************************************* Maps Geocode ***********************************/
data class MapsGeocodeResponse(
    @SerializedName("results") var mapsGeocodeResults: List<MapsGeocodeResults?>?
)

data class MapsGeocodeResults(
    @SerializedName("address_components") var addressComponents: List<AddressComponent?>?,
    @SerializedName("formatted_address") var formattedAddress: String?,
    @SerializedName("geometry") var geometry: Geometry?,
    @SerializedName("place_id") var placeId: String?,
    @SerializedName("types") var types: List<String?>?
)

data class AddressComponent(
    @SerializedName("long_name") var longName: String?,
    @SerializedName("short_name") var shortName: String?,
    @SerializedName("types") var types: List<String?>?
)

data class Geometry(
    @SerializedName("location") var location: Location?,
    @SerializedName("location_type") var locationType: String?,
    @SerializedName("viewport") var viewport: Viewport?
)

data class Viewport(
    @SerializedName("northeast") var northeast: Location?,
    @SerializedName("southwest") var southwest: Location?
)

/********************************* General ***************************************/
data class Location(
    @SerializedName("lat") var lat: Double?,
    @SerializedName("lng") var lng: Double?
)

/****************************** Elevation *************************************/
data class MapsElevationResponse(
    @SerializedName("results") var mapsGeocodeResults: List<MapsElevationResults?>?
)

data class MapsElevationResults(
    @SerializedName("elevation") val elevation: String?,
    @SerializedName("location") val location: Location?,
    @SerializedName("resolution") val resolution: String?,
)