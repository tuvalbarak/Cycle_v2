package com.tdp.cycle.features.chat

import android.os.Bundle
import android.view.View
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentChatBinding

class ChatFragment : CycleBaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding?.apply {
            chatEmptyState.show()
        }
    }

}