package com.tdp.cycle.models.cycle_server

data class ServerResponse<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null
)