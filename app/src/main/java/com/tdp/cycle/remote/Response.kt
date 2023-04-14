package com.tdp.cycle.remote

//this class will be a wrapper to the response from the server
//using sealed class to make sure only the classes here can inherit from Response
sealed class Response<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Response<T>(data)
    // sometimes its error but data is not null
    class Error<T>(data: T? = null, message: String) : Response<T>(data, message)
}