package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gamification(
    val amount: Int?,
    val description: String?
) : Parcelable
