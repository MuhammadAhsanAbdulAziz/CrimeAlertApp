package com.example.crimealert.api

import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ComplaintApi {

    @GET("/complaints/pending")
    suspend fun getPendingComplaint() : Response<List<ComplaintResponse>>

    @GET("/complaints/completed")
    suspend fun getCompletedComplaint() : Response<List<ComplaintResponse>>

    @GET("/complaints/all")
    suspend fun getallComplaint() : Response<List<ComplaintResponse>>

    @POST("/complaints")
    suspend fun addComplaint(@Body complaintRequest: ComplaintRequest) : Response<ComplaintResponse>

    @PUT("/complaints/{ComplaintId}")
    suspend fun updateComplaint(@Path("ComplaintId") ComplaintId:String, @Body complaintRequest: ComplaintRequest) : Response<ComplaintResponse>

    @DELETE("/complaints/{ComplaintId}")
    suspend fun deleteComplaint(@Path("ComplaintId") ComplaintId:String)
}