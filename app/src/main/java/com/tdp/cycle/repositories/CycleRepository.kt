package com.tdp.cycle.repositories

import com.tdp.cycle.models.cycle_server.*
import com.tdp.cycle.remote.ICycleService
import javax.inject.Inject

class CycleRepository @Inject constructor(
    private val cycleService: ICycleService
) {

    suspend fun getDrivers() = cycleService.getDrivers()
    suspend fun getDriverById(driverId: Long) = cycleService.getDriverById(driverId)
    suspend fun createDriver(driver: Driver) = cycleService.createDriver(driver)
    suspend fun updateDriver(driver: Driver) = cycleService.updateDriver(driver.id ?: -1, driver)

    suspend fun getVehiclesMeta() = cycleService.getVehiclesMeta().data?.onEach { it?.isSelected = false }
    suspend fun getVehicleById(vehicleId: Long) = cycleService.getVehicleById(vehicleId)
    suspend fun createVehicleMeta(vehicleMeta: VehicleMeta) = cycleService.createVehicleMeta(vehicleMeta)

    suspend fun getElectricVehicles() = cycleService.getElectricVehicles().data?.onEach { it?.isSelected = false }
    suspend fun getElectricVehicleById(vehicleId: Long) = cycleService.getElectricVehiclesById(vehicleId)
    suspend fun createElectricVehicle(electricVehicle: ElectricVehicleRequest) = cycleService.createElectricVehicle(electricVehicle)

    suspend fun getBatteries() = cycleService.getBatteries()
    suspend fun getBatteryById(id: Long?) = cycleService.getBatteries().data?.find { it?.id == id }
    suspend fun createBattery(battery: Battery) = cycleService.createBattery(battery)
}