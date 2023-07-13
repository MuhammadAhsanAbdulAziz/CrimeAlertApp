package com.example.crimealert.api

import com.example.crimealert.models.User
import com.example.crimealert.models.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("/users")
    suspend fun getUser() : Response<User>

    @PUT("/users/{userId}")
    suspend fun updateUser(@Path("userId") userId:String, @Body userRequest: UserRequest) : Response<User>

    @DELETE("/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId:String) : Response<User>

}