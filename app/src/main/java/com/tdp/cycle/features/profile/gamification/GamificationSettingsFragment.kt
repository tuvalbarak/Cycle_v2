package com.tdp.cycle.features.profile.gamification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentGamificationSettingsBinding
import com.tdp.cycle.features.charging_station.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamificationSettingsFragment : CycleBaseFragment<FragmentGamificationSettingsBinding>(FragmentGamificationSettingsBinding::inflate) {

    private val gamificationViewModel: GamificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        initGamificationsAdapter()
    }

    private fun initGamificationsAdapter() {
        binding?.apply {
            gamificationRV.layoutManager = LinearLayoutManager(context)
            gamificationRV.adapter = GamificationsAdapter()
        }
    }

    private fun initObservers() {
        binding?.apply {
            gamificationViewModel.gamifications.observe(viewLifecycleOwner) { gamifications ->
                if(gamifications.isNullOrEmpty()) {
                    gamificationEmptyState.show()
                    gamificationRV.gone()
                } else {
                    gamificationEmptyState.gone()
                    gamificationRV.show()
                    (gamificationRV.adapter as? GamificationsAdapter)?.submitList(gamifications)
                }
            }
        }

    }

//    viewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
//        handleProgress(isLoading)
//    }

}