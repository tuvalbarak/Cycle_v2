package com.tdp.cycle.features.profile.my_vehicles

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.customviews.CustomEmptyState
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentMyVehiclesBinding
import com.tdp.cycle.models.cycle_server.ElectricVehicle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyVehiclesFragment :
    CycleBaseFragment<FragmentMyVehiclesBinding>(FragmentMyVehiclesBinding::inflate),
    CustomEmptyState.ButtonsClickListener
{

    private var myVehiclesAdapter: MyVehiclesAdapter? = null
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

            myVehiclesEmptyState.setButtonsListener(this@MyVehiclesFragment)
        }
    }

    private fun initAdapter() {
        val onMyVehicleSelected: ((ElectricVehicle) -> Unit) = { ev ->
            myVehiclesViewModel.onMyVehicleSelected(ev)
        }
        myVehiclesAdapter = MyVehiclesAdapter(onMyVehicleSelected, context)
        binding?.apply {
            myVehiclesRV.layoutManager = LinearLayoutManager(context)
            myVehiclesRV.adapter = myVehiclesAdapter
        }
    }

    private fun initObservers() {
        myVehiclesViewModel.myElectricVehicles.observe(viewLifecycleOwner) { evs ->
            binding?.apply {
                if(evs.isNullOrEmpty()) {
                    myVehiclesEmptyState.show()
                    myVehiclesFab.gone()
                    myVehiclesRV.gone()
                } else {
                    myVehiclesEmptyState.gone()
                    myVehiclesFab.show()
                    myVehiclesRV.show()
                    myVehiclesAdapter?.submitList(evs?.sortedByDescending { (it?.isSelected) })
                }
            }
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

    companion object {
        private const val TAG = "MyVehiclesFragmentTAG"
    }

    override fun firstButtonClick() {
        myVehiclesViewModel.onFabClicked()
    }
}