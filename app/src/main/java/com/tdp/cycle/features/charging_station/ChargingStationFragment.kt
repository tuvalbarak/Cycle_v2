package com.tdp.cycle.features.charging_station

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.AnimationUtil
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.hideKeyboard
import com.tdp.cycle.common.isNull
import com.tdp.cycle.common.logd
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.FragmentChargingStationBinding
import com.tdp.cycle.models.cycle_server.ChargingStation
import com.tdp.cycle.models.cycle_server.ChargingStationStatus
import com.tdp.cycle.models.cycle_server.Comment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


@AndroidEntryPoint
class ChargingStationFragment: CycleBaseFragment<FragmentChargingStationBinding>(FragmentChargingStationBinding::inflate) {

    private val chargingStationViewModel: ChargingStationsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUi() {
        initCommentsAdapter()
        initSpinner()
        handleArguments()
        binding?.apply {
            chargingStationSendMessage.setOnClickListener {

            }

            chargingStationCommentIcon.setOnClickListener {
                val text = chargingStationLeaveAComment.text.toString()
                if(text.isBlank()) {
                    AnimationUtil().shakeView(chargingStationLeaveAComment)
                } else {
                    chargingStationViewModel.postComment(text)
                    chargingStationLeaveAComment.text.clear()
                    hideKeyboard()
                }
            }

            chargingStationLeaveARating.setOnClickListener {
                chargingStationViewModel.postRating(chargingStationRatingBar.rating.toInt())
            }

        }
    }

    private var isSpinnerInitialized = false

    private fun initSpinner() {
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.charging_station_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding?.chargingStationStatusSpinner?.adapter = adapter
            binding?.chargingStationStatusSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
                    if(isSpinnerInitialized) {
                        val status = when(selectedItem) {
                            ChargingStationStatus.AVAILABLE.ordinal -> ChargingStationStatus.AVAILABLE.value
                            ChargingStationStatus.BROKEN.ordinal -> ChargingStationStatus.BROKEN.value
                            else -> ChargingStationStatus.OCCUPIED.value
                        }
                        chargingStationViewModel.onChargingStationStatusChanged(status)
                    }
                    isSpinnerInitialized = true

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
    }

    private fun handleArguments() {
        val navArgs: ChargingStationFragmentArgs by navArgs()
        chargingStationViewModel.updateChargingStation(navArgs.chargingStation)
    }

    private fun initCommentsAdapter() {
        binding?.apply {
            chargingStationCommentsRV.layoutManager = LinearLayoutManager(context)
            chargingStationCommentsRV.adapter = CommentsAdapter()
        }
    }

    private fun initObservers() {
        chargingStationViewModel.chargingStation.observe(viewLifecycleOwner) { chargingStation ->
            binding?.apply {
                chargingStation?.let {
                    chargingStationName.text = chargingStation.name
                    chargingStationPower.text = chargingStation.power.toString()
                    chargingStationPrice.text = chargingStation.priceDetails
                    chargingStationAddress.text = "${chargingStation.address}, ${chargingStation.city}"
                    chargingStationCondition.text = chargingStation.condition
                    chargingStationChargers.text = "${chargingStation.count.toString()} Chargers"
                    chargingStation.owner?.let { user ->
                        chargingStationSendMessage.setText("Message ${user.name}")
                        chargingStationSendMessage.show()
                    } ?: run {
                        chargingStationSendMessage.gone()
                    }

                    val defaultSelectedPosition = when(chargingStation.condition) {
                        ChargingStationStatus.AVAILABLE.value -> 0
                        ChargingStationStatus.BROKEN.value -> 1
                        else -> 2
                    }
                    binding?.chargingStationStatusSpinner?.setSelection(defaultSelectedPosition)

                    handleRatings(chargingStation.ratings)
                    handleComments(chargingStation.comments)
                }
            }
        }

        chargingStationViewModel.commentPosted.observe(viewLifecycleOwner) { isCommentPosted ->
            binding?.apply {
                if(isCommentPosted) {
                    Snackbar.make(root, "Comment was posted successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        chargingStationViewModel.ratingPosted.observe(viewLifecycleOwner) { isRatingPosted ->
            binding?.apply {
                if(isRatingPosted) {
                    Snackbar.make(root, "Rating was posted successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        chargingStationViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }
    }

    private fun handleRatings(ratings: List<Float?>?) {
        binding?.apply {
            if(ratings.isNullOrEmpty()) {
                chargingStationRating.gone()
            } else {
                var sum = 0f
                ratings.forEach { sum += (it ?: 0f) }
                val average = DecimalFormat("#.##").format (sum / ratings.size)
                chargingStationRating.text = "$average (${ratings.size})"
                chargingStationRating.show()
            }
        }
    }

    private fun handleComments(comments: List<Comment?>?) {
        (binding?.chargingStationCommentsRV?.adapter as? CommentsAdapter)?.submitList(comments)
    }
}