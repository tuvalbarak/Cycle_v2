package com.tdp.cycle.models.cycle_server

data class AuthRequest(
    val google_id: String,
    val email: String,
    val name: String,
    val phone: String
)
