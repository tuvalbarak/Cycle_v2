package com.tdp.cycle.features.profile.personal_information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.databinding.FragmentPersonalInformationBinding
import com.tdp.cycle.features.profile.ProfileViewModel
import com.tdp.cycle.models.cycle_server.User
import com.tdp.cycle.models.cycle_server.UserRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInformationFragment : CycleBaseFragment<FragmentPersonalInformationBinding>(FragmentPersonalInformationBinding::inflate) {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            personalInformationSave.setOnClickListener {
                val userRequest = UserRequest(
                    name = personalInformationName.getText(),
                    phone = personalInformationPhone.getText()
                )
                profileViewModel.onSaveButtonClicked(userRequest)
                popBackStack()
            }
        }
    }

    private fun initObservers() {
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                prefillUi(it)
            }
        }

        profileViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }

//        profileViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
//            event?.getContentIfNotHandled()?.let { navigation ->
//                when(navigation) {
//                   ProfileViewModel.NavigationEvent.POP_BACKSTACK -> popBackStack()
//                   else -> { }
//                }
//            }
//        }
    }

    private fun prefillUi(user: User) {
        binding?.apply {
            personalInformationName.setText(user.name)
            personalInformationEmail.setText(user.email)
            personalInformationPhone.setText(user.phone)
        }
    }


}