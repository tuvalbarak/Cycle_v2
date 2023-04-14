package com.tdp.cycle.models.responses

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long?,
    @SerializedName("last_updated") val lastUpdated: String?,
    @SerializedName("temp_c") val tempC: Double?,
    @SerializedName("temp_f") val tempF: Double?,
    @SerializedName("condition") val condition: WeatherCondition?,
    @SerializedName("humidity") val humidityPercentage: Double,
    @SerializedName("wind_dir") val windDirection: String?,
    @SerializedName("wind_kph") val windKph: Double?,
    @SerializedName("wind_mph") val windMph: Double?,
    @SerializedName("wind_degree") val windDegree: Double?,


)

data class WeatherCondition(
    @SerializedName("text") val text: String?,
    @SerializedName("icon") val icon: String,
    @SerializedName("code") val code: Int?
)


//Example of response - we don't catch all the fields...
//{
//    "location": {
//    "name": "Fureidis",
//    "region": "Hefa",
//    "country": "Israel",
//    "lat": 32.56,
//    "lon": 35.08,
//    "tz_id": "Asia/Jerusalem",
//    "localtime_epoch": 1676721002,
//    "localtime": "2023-02-18 13:50"
//},
//    "current": {
//    "last_updated_epoch": 1676720700,
//    "last_updated": "2023-02-18 13:45",
//    "temp_c": 17.0,
//    "temp_f": 62.6,
//    "is_day": 1,
//    "condition": {
//        "text": "Sunny",
//        "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//        "code": 1000
//    },
//    "wind_mph": 12.5,
//    "wind_kph": 20.2,
//    "wind_degree": 350,
//    "wind_dir": "N",
//    "pressure_mb": 1026.0,
//    "pressure_in": 30.3,
//    "precip_mm": 0.0,
//    "precip_in": 0.0,
//    "humidity": 55,
//    "cloud": 0,
//    "feelslike_c": 17.0,
//    "feelslike_f": 62.6,
//    "vis_km": 10.0,
//    "vis_miles": 6.0,
//    "uv": 5.0,
//    "gust_mph": 13.2,
//    "gust_kph": 21.2
//},
//    "forecast": {
//    "forecastday": [
//    {
//        "date": "2023-02-18",
//        "date_epoch": 1676678400,
//        "day": {
//        "maxtemp_c": 16.8,
//        "maxtemp_f": 62.2,
//        "mintemp_c": 9.4,
//        "mintemp_f": 48.9,
//        "avgtemp_c": 12.7,
//        "avgtemp_f": 54.9,
//        "maxwind_mph": 13.0,
//        "maxwind_kph": 20.9,
//        "totalprecip_mm": 0.0,
//        "totalprecip_in": 0.0,
//        "totalsnow_cm": 0.0,
//        "avgvis_km": 10.0,
//        "avgvis_miles": 6.0,
//        "avghumidity": 64.0,
//        "daily_will_it_rain": 0,
//        "daily_chance_of_rain": 0,
//        "daily_will_it_snow": 0,
//        "daily_chance_of_snow": 0,
//        "condition": {
//        "text": "Sunny",
//        "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//        "code": 1000
//    },
//        "uv": 4.0
//    },
//        "astro": {
//        "sunrise": "06:20 AM",
//        "sunset": "05:28 PM",
//        "moonrise": "05:07 AM",
//        "moonset": "03:14 PM",
//        "moon_phase": "Waning Crescent",
//        "moon_illumination": "8",
//        "is_moon_up": 0,
//        "is_sun_up": 0
//    },
//        "hour": [
//        {
//            "time_epoch": 1676671200,
//            "time": "2023-02-18 00:00",
//            "temp_c": 9.9,
//            "temp_f": 49.8,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 7.6,
//            "wind_kph": 12.2,
//            "wind_degree": 34,
//            "wind_dir": "NE",
//            "pressure_mb": 1028.0,
//            "pressure_in": 30.35,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 71,
//            "cloud": 0,
//            "feelslike_c": 8.2,
//            "feelslike_f": 46.8,
//            "windchill_c": 8.2,
//            "windchill_f": 46.8,
//            "heatindex_c": 9.9,
//            "heatindex_f": 49.8,
//            "dewpoint_c": 4.9,
//            "dewpoint_f": 40.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 15.4,
//            "gust_kph": 24.8,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676674800,
//            "time": "2023-02-18 01:00",
//            "temp_c": 9.7,
//            "temp_f": 49.5,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 7.2,
//            "wind_kph": 11.5,
//            "wind_degree": 36,
//            "wind_dir": "NE",
//            "pressure_mb": 1028.0,
//            "pressure_in": 30.34,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 71,
//            "cloud": 0,
//            "feelslike_c": 8.0,
//            "feelslike_f": 46.4,
//            "windchill_c": 8.0,
//            "windchill_f": 46.4,
//            "heatindex_c": 9.7,
//            "heatindex_f": 49.5,
//            "dewpoint_c": 4.6,
//            "dewpoint_f": 40.3,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.5,
//            "gust_kph": 23.4,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676678400,
//            "time": "2023-02-18 02:00",
//            "temp_c": 9.6,
//            "temp_f": 49.3,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 6.9,
//            "wind_kph": 11.2,
//            "wind_degree": 43,
//            "wind_dir": "NE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.33,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 70,
//            "cloud": 0,
//            "feelslike_c": 8.0,
//            "feelslike_f": 46.4,
//            "windchill_c": 8.0,
//            "windchill_f": 46.4,
//            "heatindex_c": 9.6,
//            "heatindex_f": 49.3,
//            "dewpoint_c": 4.5,
//            "dewpoint_f": 40.1,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.3,
//            "gust_kph": 23.0,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676682000,
//            "time": "2023-02-18 03:00",
//            "temp_c": 9.4,
//            "temp_f": 48.9,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 6.7,
//            "wind_kph": 10.8,
//            "wind_degree": 51,
//            "wind_dir": "NE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.32,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 70,
//            "cloud": 0,
//            "feelslike_c": 7.8,
//            "feelslike_f": 46.0,
//            "windchill_c": 7.8,
//            "windchill_f": 46.0,
//            "heatindex_c": 9.4,
//            "heatindex_f": 48.9,
//            "dewpoint_c": 4.1,
//            "dewpoint_f": 39.4,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.1,
//            "gust_kph": 22.7,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676685600,
//            "time": "2023-02-18 04:00",
//            "temp_c": 9.4,
//            "temp_f": 48.9,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 6.0,
//            "wind_kph": 9.7,
//            "wind_degree": 58,
//            "wind_dir": "ENE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.32,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 68,
//            "cloud": 0,
//            "feelslike_c": 8.0,
//            "feelslike_f": 46.4,
//            "windchill_c": 8.0,
//            "windchill_f": 46.4,
//            "heatindex_c": 9.4,
//            "heatindex_f": 48.9,
//            "dewpoint_c": 3.8,
//            "dewpoint_f": 38.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 12.8,
//            "gust_kph": 20.5,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676689200,
//            "time": "2023-02-18 05:00",
//            "temp_c": 9.5,
//            "temp_f": 49.1,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 5.8,
//            "wind_kph": 9.4,
//            "wind_degree": 59,
//            "wind_dir": "ENE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 65,
//            "cloud": 0,
//            "feelslike_c": 8.2,
//            "feelslike_f": 46.8,
//            "windchill_c": 8.2,
//            "windchill_f": 46.8,
//            "heatindex_c": 9.5,
//            "heatindex_f": 49.1,
//            "dewpoint_c": 3.3,
//            "dewpoint_f": 37.9,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 12.3,
//            "gust_kph": 19.8,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676692800,
//            "time": "2023-02-18 06:00",
//            "temp_c": 9.4,
//            "temp_f": 48.9,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 5.8,
//            "wind_kph": 9.4,
//            "wind_degree": 59,
//            "wind_dir": "ENE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.32,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 63,
//            "cloud": 0,
//            "feelslike_c": 8.0,
//            "feelslike_f": 46.4,
//            "windchill_c": 8.0,
//            "windchill_f": 46.4,
//            "heatindex_c": 9.4,
//            "heatindex_f": 48.9,
//            "dewpoint_c": 2.7,
//            "dewpoint_f": 36.9,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 12.3,
//            "gust_kph": 19.8,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676696400,
//            "time": "2023-02-18 07:00",
//            "temp_c": 10.2,
//            "temp_f": 50.4,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 5.6,
//            "wind_kph": 9.0,
//            "wind_degree": 60,
//            "wind_dir": "ENE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.33,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 62,
//            "cloud": 0,
//            "feelslike_c": 9.1,
//            "feelslike_f": 48.4,
//            "windchill_c": 9.1,
//            "windchill_f": 48.4,
//            "heatindex_c": 10.2,
//            "heatindex_f": 50.4,
//            "dewpoint_c": 3.2,
//            "dewpoint_f": 37.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 11.0,
//            "gust_kph": 17.6,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676700000,
//            "time": "2023-02-18 08:00",
//            "temp_c": 12.6,
//            "temp_f": 54.7,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 5.1,
//            "wind_kph": 8.3,
//            "wind_degree": 55,
//            "wind_dir": "NE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.34,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 57,
//            "cloud": 0,
//            "feelslike_c": 12.0,
//            "feelslike_f": 53.6,
//            "windchill_c": 12.0,
//            "windchill_f": 53.6,
//            "heatindex_c": 12.6,
//            "heatindex_f": 54.7,
//            "dewpoint_c": 4.3,
//            "dewpoint_f": 39.7,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 7.2,
//            "gust_kph": 11.5,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676703600,
//            "time": "2023-02-18 09:00",
//            "temp_c": 14.1,
//            "temp_f": 57.4,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 5.6,
//            "wind_kph": 9.0,
//            "wind_degree": 40,
//            "wind_dir": "NE",
//            "pressure_mb": 1028.0,
//            "pressure_in": 30.34,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 52,
//            "cloud": 0,
//            "feelslike_c": 13.7,
//            "feelslike_f": 56.7,
//            "windchill_c": 13.7,
//            "windchill_f": 56.7,
//            "heatindex_c": 14.1,
//            "heatindex_f": 57.4,
//            "dewpoint_c": 4.4,
//            "dewpoint_f": 39.9,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 6.7,
//            "gust_kph": 10.8,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676707200,
//            "time": "2023-02-18 10:00",
//            "temp_c": 15.1,
//            "temp_f": 59.2,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 6.7,
//            "wind_kph": 10.8,
//            "wind_degree": 27,
//            "wind_dir": "NNE",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.34,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 50,
//            "cloud": 0,
//            "feelslike_c": 15.1,
//            "feelslike_f": 59.2,
//            "windchill_c": 15.1,
//            "windchill_f": 59.2,
//            "heatindex_c": 15.1,
//            "heatindex_f": 59.2,
//            "dewpoint_c": 4.9,
//            "dewpoint_f": 40.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 7.8,
//            "gust_kph": 12.6,
//            "uv": 5.0
//        },
//        {
//            "time_epoch": 1676710800,
//            "time": "2023-02-18 11:00",
//            "temp_c": 16.7,
//            "temp_f": 62.1,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 7.4,
//            "wind_kph": 11.9,
//            "wind_degree": 10,
//            "wind_dir": "N",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.33,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 51,
//            "cloud": 7,
//            "feelslike_c": 16.7,
//            "feelslike_f": 62.1,
//            "windchill_c": 16.7,
//            "windchill_f": 62.1,
//            "heatindex_c": 16.7,
//            "heatindex_f": 62.1,
//            "dewpoint_c": 6.4,
//            "dewpoint_f": 43.5,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 8.5,
//            "gust_kph": 13.7,
//            "uv": 5.0
//        },
//        {
//            "time_epoch": 1676714400,
//            "time": "2023-02-18 12:00",
//            "temp_c": 16.8,
//            "temp_f": 62.2,
//            "is_day": 1,
//            "condition": {
//            "text": "Sunny",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png",
//            "code": 1000
//        },
//            "wind_mph": 9.6,
//            "wind_kph": 15.5,
//            "wind_degree": 357,
//            "wind_dir": "N",
//            "pressure_mb": 1027.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 53,
//            "cloud": 14,
//            "feelslike_c": 16.8,
//            "feelslike_f": 62.2,
//            "windchill_c": 16.8,
//            "windchill_f": 62.2,
//            "heatindex_c": 16.8,
//            "heatindex_f": 62.2,
//            "dewpoint_c": 7.2,
//            "dewpoint_f": 45.0,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 11.2,
//            "gust_kph": 18.0,
//            "uv": 5.0
//        },
//        {
//            "time_epoch": 1676718000,
//            "time": "2023-02-18 13:00",
//            "temp_c": 16.5,
//            "temp_f": 61.7,
//            "is_day": 1,
//            "condition": {
//            "text": "Partly cloudy",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
//            "code": 1003
//        },
//            "wind_mph": 11.4,
//            "wind_kph": 18.4,
//            "wind_degree": 352,
//            "wind_dir": "N",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.29,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 57,
//            "cloud": 33,
//            "feelslike_c": 16.5,
//            "feelslike_f": 61.7,
//            "windchill_c": 16.5,
//            "windchill_f": 61.7,
//            "heatindex_c": 16.5,
//            "heatindex_f": 61.7,
//            "dewpoint_c": 7.8,
//            "dewpoint_f": 46.0,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 13.2,
//            "gust_kph": 21.2,
//            "uv": 5.0
//        },
//        {
//            "time_epoch": 1676721600,
//            "time": "2023-02-18 14:00",
//            "temp_c": 16.2,
//            "temp_f": 61.2,
//            "is_day": 1,
//            "condition": {
//            "text": "Cloudy",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/119.png",
//            "code": 1006
//        },
//            "wind_mph": 12.8,
//            "wind_kph": 20.5,
//            "wind_degree": 353,
//            "wind_dir": "N",
//            "pressure_mb": 1025.0,
//            "pressure_in": 30.28,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 59,
//            "cloud": 85,
//            "feelslike_c": 16.2,
//            "feelslike_f": 61.2,
//            "windchill_c": 16.2,
//            "windchill_f": 61.2,
//            "heatindex_c": 16.2,
//            "heatindex_f": 61.2,
//            "dewpoint_c": 8.2,
//            "dewpoint_f": 46.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.8,
//            "gust_kph": 23.8,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676725200,
//            "time": "2023-02-18 15:00",
//            "temp_c": 15.8,
//            "temp_f": 60.4,
//            "is_day": 1,
//            "condition": {
//            "text": "Overcast",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/122.png",
//            "code": 1009
//        },
//            "wind_mph": 13.2,
//            "wind_kph": 21.2,
//            "wind_degree": 355,
//            "wind_dir": "N",
//            "pressure_mb": 1025.0,
//            "pressure_in": 30.28,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 62,
//            "cloud": 100,
//            "feelslike_c": 15.8,
//            "feelslike_f": 60.4,
//            "windchill_c": 15.8,
//            "windchill_f": 60.4,
//            "heatindex_c": 15.8,
//            "heatindex_f": 60.4,
//            "dewpoint_c": 8.5,
//            "dewpoint_f": 47.3,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 15.2,
//            "gust_kph": 24.5,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676728800,
//            "time": "2023-02-18 16:00",
//            "temp_c": 15.3,
//            "temp_f": 59.5,
//            "is_day": 1,
//            "condition": {
//            "text": "Overcast",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/122.png",
//            "code": 1009
//        },
//            "wind_mph": 12.8,
//            "wind_kph": 20.5,
//            "wind_degree": 359,
//            "wind_dir": "N",
//            "pressure_mb": 1025.0,
//            "pressure_in": 30.28,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 65,
//            "cloud": 94,
//            "feelslike_c": 15.3,
//            "feelslike_f": 59.5,
//            "windchill_c": 15.3,
//            "windchill_f": 59.5,
//            "heatindex_c": 15.3,
//            "heatindex_f": 59.5,
//            "dewpoint_c": 8.8,
//            "dewpoint_f": 47.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.8,
//            "gust_kph": 23.8,
//            "uv": 4.0
//        },
//        {
//            "time_epoch": 1676732400,
//            "time": "2023-02-18 17:00",
//            "temp_c": 14.4,
//            "temp_f": 57.9,
//            "is_day": 1,
//            "condition": {
//            "text": "Cloudy",
//            "icon": "//cdn.weatherapi.com/weather/64x64/day/119.png",
//            "code": 1006
//        },
//            "wind_mph": 12.1,
//            "wind_kph": 19.4,
//            "wind_degree": 2,
//            "wind_dir": "N",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.28,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 68,
//            "cloud": 80,
//            "feelslike_c": 13.0,
//            "feelslike_f": 55.4,
//            "windchill_c": 13.0,
//            "windchill_f": 55.4,
//            "heatindex_c": 14.4,
//            "heatindex_f": 57.9,
//            "dewpoint_c": 8.6,
//            "dewpoint_f": 47.5,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 15.9,
//            "gust_kph": 25.6,
//            "uv": 3.0
//        },
//        {
//            "time_epoch": 1676736000,
//            "time": "2023-02-18 18:00",
//            "temp_c": 14.6,
//            "temp_f": 58.3,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 11.9,
//            "wind_kph": 19.1,
//            "wind_degree": 5,
//            "wind_dir": "N",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.3,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 70,
//            "cloud": 10,
//            "feelslike_c": 13.2,
//            "feelslike_f": 55.8,
//            "windchill_c": 13.2,
//            "windchill_f": 55.8,
//            "heatindex_c": 14.6,
//            "heatindex_f": 58.3,
//            "dewpoint_c": 9.2,
//            "dewpoint_f": 48.6,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.1,
//            "gust_kph": 22.7,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676739600,
//            "time": "2023-02-18 19:00",
//            "temp_c": 13.2,
//            "temp_f": 55.8,
//            "is_day": 0,
//            "condition": {
//            "text": "Partly cloudy",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/116.png",
//            "code": 1003
//        },
//            "wind_mph": 11.0,
//            "wind_kph": 17.6,
//            "wind_degree": 11,
//            "wind_dir": "NNE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 72,
//            "cloud": 30,
//            "feelslike_c": 11.6,
//            "feelslike_f": 52.9,
//            "windchill_c": 11.6,
//            "windchill_f": 52.9,
//            "heatindex_c": 13.2,
//            "heatindex_f": 55.8,
//            "dewpoint_c": 8.2,
//            "dewpoint_f": 46.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 16.6,
//            "gust_kph": 26.6,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676743200,
//            "time": "2023-02-18 20:00",
//            "temp_c": 12.1,
//            "temp_f": 53.8,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 10.5,
//            "wind_kph": 16.9,
//            "wind_degree": 12,
//            "wind_dir": "NNE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 72,
//            "cloud": 13,
//            "feelslike_c": 10.3,
//            "feelslike_f": 50.5,
//            "windchill_c": 10.3,
//            "windchill_f": 50.5,
//            "heatindex_c": 12.1,
//            "heatindex_f": 53.8,
//            "dewpoint_c": 7.2,
//            "dewpoint_f": 45.0,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 18.6,
//            "gust_kph": 29.9,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676746800,
//            "time": "2023-02-18 21:00",
//            "temp_c": 11.8,
//            "temp_f": 53.2,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 9.4,
//            "wind_kph": 15.1,
//            "wind_degree": 17,
//            "wind_dir": "NNE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 73,
//            "cloud": 1,
//            "feelslike_c": 10.1,
//            "feelslike_f": 50.2,
//            "windchill_c": 10.1,
//            "windchill_f": 50.2,
//            "heatindex_c": 11.8,
//            "heatindex_f": 53.2,
//            "dewpoint_c": 7.1,
//            "dewpoint_f": 44.8,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 16.8,
//            "gust_kph": 27.0,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676750400,
//            "time": "2023-02-18 22:00",
//            "temp_c": 11.3,
//            "temp_f": 52.3,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 7.8,
//            "wind_kph": 12.6,
//            "wind_degree": 19,
//            "wind_dir": "NNE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.31,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 73,
//            "cloud": 0,
//            "feelslike_c": 9.8,
//            "feelslike_f": 49.6,
//            "windchill_c": 9.8,
//            "windchill_f": 49.6,
//            "heatindex_c": 11.3,
//            "heatindex_f": 52.3,
//            "dewpoint_c": 6.7,
//            "dewpoint_f": 44.1,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.8,
//            "gust_kph": 23.8,
//            "uv": 1.0
//        },
//        {
//            "time_epoch": 1676754000,
//            "time": "2023-02-18 23:00",
//            "temp_c": 11.0,
//            "temp_f": 51.8,
//            "is_day": 0,
//            "condition": {
//            "text": "Clear",
//            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
//            "code": 1000
//        },
//            "wind_mph": 7.4,
//            "wind_kph": 11.9,
//            "wind_degree": 21,
//            "wind_dir": "NNE",
//            "pressure_mb": 1026.0,
//            "pressure_in": 30.3,
//            "precip_mm": 0.0,
//            "precip_in": 0.0,
//            "humidity": 73,
//            "cloud": 0,
//            "feelslike_c": 9.5,
//            "feelslike_f": 49.1,
//            "windchill_c": 9.5,
//            "windchill_f": 49.1,
//            "heatindex_c": 11.0,
//            "heatindex_f": 51.8,
//            "dewpoint_c": 6.4,
//            "dewpoint_f": 43.5,
//            "will_it_rain": 0,
//            "chance_of_rain": 0,
//            "will_it_snow": 0,
//            "chance_of_snow": 0,
//            "vis_km": 10.0,
//            "vis_miles": 6.0,
//            "gust_mph": 14.3,
//            "gust_kph": 23.0,
//            "uv": 1.0
//        }
//        ]
//    }
//    ]
//}
//}