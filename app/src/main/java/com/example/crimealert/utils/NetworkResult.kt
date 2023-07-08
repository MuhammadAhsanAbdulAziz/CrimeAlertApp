package com.example.crimealert.utils

sealed class NetworkResult<T>(val data:T? = null, val error: String? = null)
{
    class Loading<T> : NetworkResult<T>()
    class Success<T>(data: T? = null) : NetworkResult<T>(data = data)
    class Error<T>(errorMessage : String) : NetworkResult<T>(error = errorMessage)
}