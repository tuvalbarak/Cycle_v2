package com.tdp.cycle.features.profile.gamification

import android.os.Bundle
import android.view.View
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentGamificationSettingsBinding

class GamificationSettingsFragment : CycleBaseFragment<FragmentGamificationSettingsBinding>(FragmentGamificationSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            gamificationEmptyState.show()
        }
    }

    private fun initObservers() {

    }

//    viewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
//        handleProgress(isLoading)
//    }

}