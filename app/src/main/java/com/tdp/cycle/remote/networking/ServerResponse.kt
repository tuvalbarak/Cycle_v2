package com.tdp.cycle.remote.networking

import okhttp3.Headers

data class ServerResponse<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null
)

sealed class ResponseResult<T>

// headers will be populated only in api calls that return retrofit2.Response
class RemoteResponseSuccess<T>(
    var data: T? = null,
) : ResponseResult<T>()

class RemoteResponseError<T>(
    var error: MTError
) : ResponseResult<T>()

//sealed class LocalResponse
data class LocalResponseSuccess<T>(val data: T? = null) : ResponseResult<T>()
data class LocalResponseError<T>(val error: String? = null) : ResponseResult<T>()

sealed class MTResponse<T> {
    companion object {
        fun <T> fromResponseResult(result: ResponseResult<T>) = when (result) {
            is RemoteResponseSuccess -> ResponseSuccess(result.data)
            is LocalResponseSuccess -> ResponseSuccess(result.data)
            is LocalResponseError -> ResponseError(result.error)
            is RemoteResponseError -> ResponseError("Error")
        }
    }
}

data class ResponseSuccess<T>(val data: T? = null) : MTResponse<T>()
data class ResponseError<T>(val error: String? = null) : MTResponse<T>()

