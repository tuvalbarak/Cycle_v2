package com.tdp.cycle.remote.networking

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * RemoteResponseHandler: responsible to handle safely remote/api calls,
 * Wrap success calls in ResponseSuccess
 * Wrap failed/exception calls in ResponseError
 * Timber log the relevant parameters
 * */

class RemoteResponseHandler {

    /**
     * Handle main-safe remote/api calls, making sure calls are handle in Dispatcher.io.
     * @param apiCall -> api call that return ServerResponse result
     * Use with only one api call
     * @return ResponseResult -> ResponseSuccess on success , ResponseError in case of error
     * */
    suspend fun <Type> safeApiCall(
        apiCall: (suspend () -> ServerResponse<Type>)? = null
    ): ResponseResult<Type> {
        return withContext(Dispatchers.IO) {
            try {
                val data: Type? = apiCall?.invoke()?.data
                return@withContext RemoteResponseSuccess<Type>(data = data)
            } catch (ex: Exception) {
                handleException(ex, "ERROR")
            }
        }
    }

    /**
     * Handle Exception
     * @param ex -> the exception from the api call
     * @param timberTag -> "ERROR" or "SUCCESS"
     * @return ResponseResult -> ResponseError
     * */
    private fun <T> handleException(ex: Exception, timberTag: String): ResponseResult<T> {
        when (ex) {
            is HttpException -> {
                try {
                    /** Trying to parse FormattedError **/
                    val errorData = ex.response()?.errorBody()?.string()
                    val formattedError = Gson().fromJson(errorData, FormattedError::class.java)
                    if (formattedError.message.isNotEmpty()) {
                        Log.d(timberTag, "\"ERROR!! Formatted Error code: ${formattedError.code}, message: ${formattedError.message}")
                        return RemoteResponseError(formattedError)
                    }
                } catch (ex: Exception) {
                    /** Error in parsing, continue to HTTPError **/
                }

                val httpError = when (ex.code()) {
                    204 -> RemoteResponseError<T>(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.NO_CONTENT))
                    400 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.BAD_REQUEST))
                    401 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.UNAUTHORIZED))
                    403 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.FORBIDDEN))
                    404 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.NOT_FOUND))
                    500 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.UNDER_MAINTENANCE))
                    503 -> RemoteResponseError(HTTPError(code = ex.code(), message = ex.message(), type = HTTPErrorType.SERVER_DOWN))
                    else -> RemoteResponseError(
                        HTTPError(
                            code = ex.code(),
                            message = ex.message(),
                            type = HTTPErrorType.UNKNOWN_ERROR
                        )
                    )
                }
                (httpError.error as? HTTPError).let { error ->
                    Log.d(timberTag, "\"ERROR!! Http Error code: ${error?.code}, type: ${error?.type?.name}, message:  ${error?.message}")
                }
                return httpError
            }
            is UnknownHostException -> {
                Log.d(timberTag, "\"ERROR!! NetworkError ${ex.localizedMessage}")
                return RemoteResponseError(NetworkError)
            }
            is KotlinNullPointerException , is NullPointerException -> {
                Log.d(timberTag, "\"SUCCESS - Empty Response\"")
                return RemoteResponseSuccess(null)
            }
            is IllegalArgumentException -> {
                Log.d(timberTag, "\"ERROR!! IllegalArgument ${ex.localizedMessage}")
                return RemoteResponseError(IllegalArgument)
            }
            else -> {
                Log.d(timberTag, "\"ERROR!! UnknownError ${ex.localizedMessage}")
                return RemoteResponseError(UnknownError)
            }
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun <Type> getTimberTag(
        apiCall: (suspend () -> ServerResponse<Type>)? = null,
    ): String {

        var methodName: String? = null
        var className: String? = null

        apiCall?.let {
            methodName = apiCall.javaClass.enclosingMethod.name
            className = apiCall.javaClass.enclosingClass.simpleName
        }

        return "Timber - ResponseHandler.($className.kt:1) $className.$methodName()"
    }

}
