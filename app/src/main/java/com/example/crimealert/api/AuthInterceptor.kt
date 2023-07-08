package com.example.crimealert.api

import com.example.crimealert.utils.UtilManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var utilManager: UtilManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = utilManager.getToken()
        request.addHeader("Authorization", "Bearer $token")

        return chain.proceed(request.build())
    }
}