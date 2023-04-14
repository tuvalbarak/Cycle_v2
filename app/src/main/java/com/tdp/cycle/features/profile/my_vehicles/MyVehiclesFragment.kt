package com.tdp.cycle.features.profile.my_vehicles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentMyVehiclesBinding
import com.tdp.cycle.features.splash.SplashFragmentDirections
import com.tdp.cycle.models.ElectricVehicleModel
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import com.tdp.cycle.models.cycle_server.VehicleMeta
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyVehiclesFragment : CycleBaseFragment<FragmentMyVehiclesBinding>(FragmentMyVehiclesBinding::inflate) {

    private var myVehiclesAdapter: AddVehiclesAdapter? = null
    private val myVehiclesViewModel: MyVehiclesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        initAdapter()
        binding?.apply {
            myVehiclesFab.setOnClickListener {
                myVehiclesViewModel.onFabClicked()
            }
        }
    }

    private fun initAdapter() {
        val onMyVehicleSelected: ((VehicleMeta) -> Unit) = { ev ->
            myVehiclesViewModel.onMyVehicleSelected(ev)
        }
        myVehiclesAdapter = AddVehiclesAdapter(onMyVehicleSelected, context)
        binding?.apply {
            myVehiclesRV.layoutManager = LinearLayoutManager(context)
            myVehiclesRV.adapter = myVehiclesAdapter
        }
    }

    private fun initObservers() {
        myVehiclesViewModel.myVehicles.observe(viewLifecycleOwner) { evs ->
            myVehiclesAdapter?.submitList(evs?.sortedByDescending { (it?.isSelected) })
        }

        myVehiclesViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { navigation ->
                when(navigation) {
                    MyVehiclesViewModel.NavigationEvent.GO_TO_ADD_VEHICLE -> {
                        findNavController().safeNavigate(MyVehiclesFragmentDirections.actionMyVehicleFragmentToAddVehicleFragment())
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