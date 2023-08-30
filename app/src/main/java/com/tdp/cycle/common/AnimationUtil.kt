package com.tdp.cycle.common

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class AnimationUtil {

    fun shakeView(view: View) {
        ObjectAnimator.ofFloat(
            view, "translationX",
            0f, -30f, 30.0f, -15.0f, 15.0f, -5.0f, 5.0f, 0f,
        ).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = 0
            start()
        }
    }
}