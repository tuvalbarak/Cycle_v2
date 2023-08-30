package com.tdp.cycle.remote.networking

sealed class MTError

class FormattedError(
    val code: Int,
    val message: String,
) : MTError()

object IllegalArgument : MTError()
object NetworkError : MTError()
object UnknownError : MTError()
object NullPointer : MTError()


data class HTTPError(
    val code: Int,
    val message: String,
    val type: HTTPErrorType
) : MTError()

enum class HTTPErrorType {
    NOT_FOUND,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NO_CONTENT,
    UNDER_MAINTENANCE,
    SERVER_DOWN,
    UNKNOWN_ERROR
}

fun MTError.getErrorMsgByType(): String {
    when (this) {
        is FormattedError -> {
            return message.ifEmpty { "Oops! something went wrong \nPlease try again later" }
        }
        is HTTPError -> {
            return "Oops! something went wrong \nPlease try again later"
        }
        is NetworkError -> {
            return "Oops! Network Error \nPlease check your network connection and try again"
        }
        is UnknownError -> {
            return "Oops! something went wrong \nPlease try again later"
        }
        else -> {}
    }
    return "Oops! something went wrong \nPlease try again later"
}