package com.tdp.cycle.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.tdp.cycle.models.ChargingStationRealModel
import javax.inject.Inject

class ChargingStationsRepository @Inject constructor() {

    private var database: DatabaseReference? = null
    private val stations = MutableLiveData<List<ChargingStationRealModel?>?>()

    fun getStations() = stations

    fun fetchChargingStationsLocations() {
        database = Firebase.database.reference
        database?.addValueEventListener(dbListener)
    }

    private val dbListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            //Public stations from firebase
            val publicStations = dataSnapshot.getValue<List<ChargingStationRealModel>>()
            val privateStations = getPrivateStations()
            stations.postValue(publicStations?.plus(privateStations))
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    }

    private fun getPrivateStations() = listOf(
        ChargingStationRealModel(station_id = 5000, station_name = "Cohen's'", lat = 32.09015, lng = 34.80414, station_access = "private"),
        ChargingStationRealModel(station_id = 5001, station_name = "Ben-Yosef's'", lat = 32.1647, lng = 34.85234, station_access = "private"),
        ChargingStationRealModel(station_id = 5002, station_name = "David's'", lat = 32.30965, lng = 34.8779, station_access = "private"),
        ChargingStationRealModel(station_id = 5003, station_name = "Hadad's'", lat = 32.43269, lng = 34.91685, station_access = "private"),
        ChargingStationRealModel(station_id = 5004, station_name = "Bialik's'", lat = 32.43163, lng = 34.91858, station_access = "private"),
        ChargingStationRealModel(station_id = 5005, station_name = "Pezos's'", lat = 32.47039, lng = 34.95792, station_access = "private"),
//        ChargingStationRealModel(station_id = 5006, station_name = "Barak's'", lat = 32.96051, lng = 34.96051, station_access = "private"),
        ChargingStationRealModel(station_id = 5007, station_name = "Israeli's'", lat = 32.68482, lng = 34.94089, station_access = "private"),
        ChargingStationRealModel(station_id = 5008, station_name = "Levi's'", lat = 32.68695, lng = 34.94237, station_access = "private")
    )

    companion object {
        const val TAG = "ChargingStationRepositoryTAG"
    }
}