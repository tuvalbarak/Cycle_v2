package com.tdp.cycle.models.cycle_server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: Long? = null,
    val content: String? = null,
    val commentator: String? = null,
    val chargingStationId: Long? = null
) : Parcelable

data class CommentRequest(
    val content: String,
    val commentator: String,
)