package com.tdp.cycle.features.profile.my_vehicles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentAddVehicleBinding
import com.tdp.cycle.models.ElectricVehicleModel
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.VehicleMeta
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddVehicleFragment : CycleBaseFragment<FragmentAddVehicleBinding>(FragmentAddVehicleBinding::inflate) {

    private val myVehiclesViewModel: MyVehiclesViewModel by activityViewModels()
    private var addVehiclesAdapter: AddVehiclesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        initAdapter()

        binding?.apply {
            addVehicleButton.setOnClickListener {
                myVehiclesViewModel.onAddVehiclesClicked()
            }
        }

    }

    private fun initAdapter() {
        val onMyVehicleSelected: ((VehicleMeta) -> Unit) = { ev ->
            myVehiclesViewModel.onAddVehicleSelected(ev)
        }
        addVehiclesAdapter = AddVehiclesAdapter(onMyVehicleSelected, context)
        binding?.apply {
            addVehiclesRV.layoutManager = LinearLayoutManager(context)
            addVehiclesRV.adapter = addVehiclesAdapter
        }
    }

    private fun initObservers() {
        myVehiclesViewModel.addVehicles.observe(viewLifecycleOwner) { evs ->
            addVehiclesAdapter?.submitList(evs)
        }

        myVehiclesViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { navigation ->
                when(navigation) {
                    MyVehiclesViewModel.NavigationEvent.GO_TO_MY_VEHICLE -> {
                        activity?.onBackPressed()
                    }
                    else -> { }
                }
            }
        }

        myVehiclesViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

    }

}