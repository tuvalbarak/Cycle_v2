package com.tdp.cycle.common

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tdp.cycle.R

object StarsHelper {
    fun Context.handleStars(images: List<ImageView?>, rating: Double) {
        val blackStar = ContextCompat.getDrawable(this, R.drawable.ic_icon_star_blue)
        val greyStar = ContextCompat.getDrawable(this, R.drawable.ic_icon_star_grey)
        val halfStar = ContextCompat.getDrawable(this, R.drawable.ic_icon_star_half)

        images.forEach { it?.setImageDrawable(greyStar) }

        if(rating == 0.0) {
            return
        }

        run breaking@ {
            listOf(1.0, 2.0, 3.0, 4.0, 5.0).forEachIndexed { index, value ->
                if(rating < value) {
                    images[index]?.setImageDrawable(halfStar)
                    return@breaking
                }
                if(rating >= value) {
                    images[index]?.setImageDrawable(blackStar)
                    if(rating == value) return@breaking
                }
            }
        }
    }
}