package com.tdp.cycle.repositories

import com.tdp.cycle.R
import com.tdp.cycle.models.Battery
import com.tdp.cycle.models.ElectricVehicleModel
import com.tdp.cycle.models.Model

class ElectricVehiclesRepository {

    fun getMyElectricVehicles() = listOf(
        ElectricVehicleModel(
            1,
            "Tesla",
            Model("Y", R.drawable.ic_tesla_y , "PERFORMANCE AWS", 2020),
            Battery(507.0, 80.5, 0.172, 85.0),
            isSelected = true
        ),
        ElectricVehicleModel(
            2,
            "MG",
            Model("MARVEL R", R.drawable.ic_mg_marvel, "PERFORMANCE", 2021),
            Battery(335.0, 65.0, 0.194, 30.0),
            isSelected = false
        )
    )

    fun getAllElectricVehicles() = listOf(
        ElectricVehicleModel(
            1,
            "Tesla",
            Model("Y", R.drawable.ic_tesla_y , "PERFORMANCE AWS", 2020),
            Battery(507.0, 80.5, 0.172, 85.0),
            isSelected = false
        ),
        ElectricVehicleModel(
            2,
            "MG",
            Model("MARVEL R", R.drawable.ic_mg_marvel, "PERFORMANCE", 2021),
            Battery(335.0, 65.0, 0.194, 30.0),
            isSelected = false
        ),
        ElectricVehicleModel(
            3,
            "BMW",
            Model("GOGO", R.drawable.ic_mg_marvel, "THE BEST", 2021),
            Battery(335.0, 65.0, 0.194, 30.0),
            isSelected = false
        )
    )


}