package com.tdp.cycle.features.profile.my_vehicles

import androidx.lifecycle.MutableLiveData
import com.tdp.cycle.bases.CycleBaseViewModel
import com.tdp.cycle.common.SingleLiveEvent
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.ElectricVehicleRequest
import com.tdp.cycle.models.cycle_server.UserRequest
import com.tdp.cycle.models.cycle_server.VehicleMeta
import com.tdp.cycle.remote.networking.LocalResponseError
import com.tdp.cycle.remote.networking.LocalResponseSuccess
import com.tdp.cycle.remote.networking.RemoteResponseError
import com.tdp.cycle.remote.networking.RemoteResponseSuccess
import com.tdp.cycle.remote.networking.getErrorMsgByType
import com.tdp.cycle.repositories.UserRepository
import com.tdp.cycle.repositories.VehiclesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyVehiclesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val vehiclesRepository: VehiclesRepository
) : CycleBaseViewModel() {

    val myElectricVehicles = MutableLiveData<List<ElectricVehicle?>?>()
    val metaVehicles = MutableLiveData<List<VehicleMeta?>?>()
    val navigationEvent = SingleLiveEvent<NavigationEvent>()

    enum class NavigationEvent {
        GO_TO_ADD_VEHICLE,
        GO_TO_MY_VEHICLE
    }

    init {
        safeViewModelScopeIO {
            progressData.startProgress()
            getMyEvs()
            getMetaVehicles()
            progressData.endProgress()
        }
    }

    private suspend fun getMyEvs() {
        when(val response = vehiclesRepository.getElectricVehicles()) {
            is RemoteResponseSuccess -> {
                val vehicles = response.data
                val user = userRepository.getUserMe() as? RemoteResponseSuccess
                val userElectricVehicleId = user?.data?.myElectricVehicle
                vehicles?.find { it.id ==  userElectricVehicleId }?.let {
                    it.isSelected = true
                } ?: run {
                    vehicles?.firstOrNull()?.isSelected = true
                }
                myElectricVehicles.postValue(vehicles)
            }
            is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
            else -> { }
        }
    }

    private suspend fun getMetaVehicles() {
        when(val response = vehiclesRepository.getVehiclesMeta()) {
            is RemoteResponseSuccess -> {
                val metasThatUserDoesntHave = response.data?.filterNot { vehicleMeta ->
                    myElectricVehicles.value?.find { electricVehicle ->
                        electricVehicle?.vehicleMeta?.id ==  vehicleMeta.id
                    }?.let { true } ?: false
                }
                metaVehicles.postValue(metasThatUserDoesntHave)
            }
            is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
            else -> { }
        }
    }

    fun onMyVehicleSelected(electricVehicle: ElectricVehicle) {
        safeViewModelScopeIO {
            val userRequest = UserRequest(
                my_electric_vehicle = electricVehicle.id
            )
            when(val response = userRepository.updateUser(userRequest)) {
                is RemoteResponseSuccess -> {
                    val updatedVehiclesList = mutableListOf<ElectricVehicle?>()
                    myElectricVehicles.value?.forEach {
                        updatedVehiclesList.add(
                            it?.copy(
                                isSelected = (it.id == electricVehicle.id)
                            )
                        )
                    }
                    myElectricVehicles.postValue(updatedVehiclesList)
                }
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
        }
    }

    fun onMetaVehicleSelected(metaVehicleMeta: VehicleMeta) {
        val updatedVehiclesList = mutableListOf<VehicleMeta?>()
        metaVehicles.value?.forEach {
            updatedVehiclesList.add(
                it?.copy(
                    isSelected = (it.id == metaVehicleMeta.id)
                )
            )
        }
        metaVehicles.postValue(updatedVehiclesList)
    }

    fun onSaveMetaVehicleClicked() {
        safeViewModelScopeIO {
            progressData.startProgress()
            val user = userRepository.getUserMe() as? RemoteResponseSuccess
            val selectedVehicleMeta = metaVehicles.value?.find { it?.isSelected == true }
            val electricVehicleRequest = ElectricVehicleRequest(
                current_battery_id = selectedVehicleMeta?.manufactureBatteryId,
                vehicle_meta_id = selectedVehicleMeta?.id,
                owner_id = user?.data?.id
            )
            when(val response = vehiclesRepository.createElectricVehicle(electricVehicleRequest)) {
                is RemoteResponseSuccess -> {
//                    userRepository.updateUser(UserRequest(my_electric_vehicle = response.data?.id))
                    getMyEvs()
                    getMetaVehicles()
                    navigationEvent.postRawValue(NavigationEvent.GO_TO_MY_VEHICLE)
                }
                is RemoteResponseError -> errorEvent.postRawValue(response.error.getErrorMsgByType())
                else -> { }
            }
            progressData.endProgress()
        }
    }


//    fun onAddVehiclesClicked() {
//        viewModelScope.launch(Dispatchers.IO) {
//            progressData.startProgress()
////            electricVehiclesRepository.getAllElectricVehicles()
//            val vehicleMeta = addVehicles.value?.firstOrNull { it?.isSelected == true }
//            vehicleMeta?.let {
//
//                //Need to create new Instance of the meta battery -
////                val metaBattery = cycleRepository.getBatteryById(vehicleMeta.manufactureBatteryId)
////
////                val newVehicle = cycleRepository.createElectricVehicle(
////                    ElectricVehicleRequest(
////                        current_battery_id = metaBattery?.id,
////                        vehicle_meta_id = vehicleMeta.id
////                    )
////                ).data
////
////                userRepositoryDepricated.updateMyVehiclesList(newVehicle)
////                val myMetaVehiclesList = mutableListOf<VehicleMeta?>()
////                cycleRepository.getElectricVehicles()?.forEach { myElectricVehicles ->
////                    myElectricVehicles?.vehicleMetaId?.let {
////                        myMetaVehiclesList.add(cycleRepository.getVehicleById(it).data)
////                    }
////                }
////                myVehicles.postValue(myMetaVehiclesList)
//
//
//            }
//
//            navigationEvent.postRawValue(NavigationEvent.GO_TO_MY_VEHICLE)
//            progressData.endProgress()
//        }
//    }

    fun onFabClicked() {
        navigationEvent.postRawValue(NavigationEvent.GO_TO_ADD_VEHICLE)
    }

}