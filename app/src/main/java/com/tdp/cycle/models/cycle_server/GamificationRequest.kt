package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamificationRequest(
    val amount: Int,
    val description: String
) : Parcelable