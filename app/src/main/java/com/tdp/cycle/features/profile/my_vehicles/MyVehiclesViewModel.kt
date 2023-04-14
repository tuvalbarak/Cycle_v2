package com.tdp.cycle.features.profile.my_vehicles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.cycle_server.ElectricVehicleRequest
import com.tdp.cycle.models.cycle_server.VehicleMeta
import com.tdp.cycle.repositories.CycleRepository
import com.tdp.cycle.repositories.ElectricVehiclesRepository
import com.tdp.cycle.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyVehiclesViewModel @Inject constructor(
    private val electricVehiclesRepository: ElectricVehiclesRepository,
    private val cycleRepository: CycleRepository,
    private val userRepository: UserRepository
) : CycleBaseViewModel() {

    val myVehicles = MutableLiveData<List<VehicleMeta?>?>()
    val addVehicles = MutableLiveData<List<VehicleMeta?>?>()
    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    enum class NavigationEvent {
        GO_TO_ADD_VEHICLE,
        GO_TO_MY_VEHICLE
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            progressData.startProgress()
            getMyEvs()
            getAddEVs()
            progressData.endProgress()
        }
    }

    private fun getMyEvs() {
        safeViewModelScopeIO {
            val myMetaVehiclesList = mutableListOf<VehicleMeta?>()
            cycleRepository.getElectricVehicles()?.forEach { myElectricVehicles ->
                myElectricVehicles?.vehicleMetaId?.let {
                    myMetaVehiclesList.add(cycleRepository.getVehicleById(it).data)
                }
            }

            //find last selected ev
            val lastSelectedMyEv = userRepository.getLastSelectedEV()
            myMetaVehiclesList.find { it?.id == lastSelectedMyEv?.id }?.isSelected = true

            myVehicles.postValue(myMetaVehiclesList)
        }
    }

    private fun getAddEVs() {
        safeViewModelScopeIO {
            val addVehiclesList = cycleRepository.getVehiclesMeta()
            val filteredAddVehicles = mutableListOf<VehicleMeta?>()
            addVehiclesList?.forEach { meta ->
                myVehicles.value?.find { it?.id == meta?.id }?.let {

                } ?: run {
                    filteredAddVehicles.add(meta)
                }
            }

            addVehicles.postValue(filteredAddVehicles)
        }
    }

    fun onMyVehicleSelected(selectedEV: VehicleMeta) {
        safeViewModelScopeIO {

            val updatedVehiclesList = mutableListOf<VehicleMeta>()
            myVehicles.value?.forEach {
                it?.let {
                    updatedVehiclesList.add(
                        it.copy(isSelected = (it.id == selectedEV.id))
                    )
                }
            }

            userRepository.updateLastSelectedEV(selectedEV)
            myVehicles.postValue(updatedVehiclesList)
        }
    }

    fun onAddVehicleSelected(newEV: VehicleMeta) {
        val updatedVehiclesList = mutableListOf<VehicleMeta>()
        addVehicles.value?.forEach {
            it?.let {
                if(it.id == newEV.id) {
                    updatedVehiclesList.add(it.copy(isSelected =  it.isSelected?.not()))
                } else {
                    updatedVehiclesList.add(it.copy())
                }
            }
        }

        addVehicles.postValue(updatedVehiclesList)
    }

    fun onAddVehiclesClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            progressData.startProgress()
//            electricVehiclesRepository.getAllElectricVehicles()
            val vehicleMeta = addVehicles.value?.firstOrNull { it?.isSelected == true }
            vehicleMeta?.let {

                //Need to create new Instance of the meta battery -
                val metaBattery = cycleRepository.getBatteryById(vehicleMeta.manufactureBatteryId)
//                val newBattery = cycleRepository.createBattery(
//                    Battery(
//                        id = null,
//                        rangeCapacity = metaBattery?.rangeCapacity,
//                        batteryCapacity = metaBattery?.batteryCapacity,
//                        percentage = 100
//                    )
//                ).data

                val newVehicle = cycleRepository.createElectricVehicle(
                    ElectricVehicleRequest(
                        current_battery_id = metaBattery?.id,
                        vehicle_meta_id = vehicleMeta.id
                    )
                ).data

                userRepository.updateMyVehiclesList(newVehicle)
                val myMetaVehiclesList = mutableListOf<VehicleMeta?>()
                cycleRepository.getElectricVehicles()?.forEach { myElectricVehicles ->
                    myElectricVehicles?.vehicleMetaId?.let {
                        myMetaVehiclesList.add(cycleRepository.getVehicleById(it).data)
                    }
                }
                myVehicles.postValue(myMetaVehiclesList)


            }

            navigationEvent.postRawValue(NavigationEvent.GO_TO_MY_VEHICLE)
            progressData.endProgress()
        }
    }

    fun onFabClicked() {
        navigationEvent.postRawValue(NavigationEvent.GO_TO_ADD_VEHICLE)
    }

}