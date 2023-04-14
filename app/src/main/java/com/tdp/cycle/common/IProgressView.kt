package com.tdp.cycle.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tdp.cycle.R
import com.tdp.cycle.databinding.CycleProgressBarBinding

interface IProgressView {
    fun handleProgress(isLoading: Boolean) {
        if (isLoading) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }

    fun showProgressBar() {}
    fun hideProgressBar() {}
}

class CycleProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IProgressView {

    private var binding: CycleProgressBarBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.cycle_progress_bar, this, true)
        binding = CycleProgressBarBinding.bind(view)
        binding.lottieAnimationView.setAnimation("loader.json")
    }

    override fun showProgressBar() {
        binding.lottieAnimationViewBkg.show()
        binding.lottieAnimationView.playAnimation()
    }

    override fun hideProgressBar() {
        binding.lottieAnimationViewBkg.gone()
        binding.lottieAnimationView.cancelAnimation()
    }

}