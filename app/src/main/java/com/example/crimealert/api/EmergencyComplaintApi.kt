package com.example.crimealert.api

import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmergencyComplaintApi {

    @GET("/EmergencyComplaintResponse/all")
    suspend fun getallEmergencyComplaint() : Response<List<EmergencyComplaintResponse>>


    @PUT("/EmergencyComplaintResponse/{ComplaintId}")
    suspend fun updateEmergencyComplaint(@Path("ComplaintId") ComplaintId:String, @Body emergencyComplaintRequest: EmergencyComplaintRequest) : Response<EmergencyComplaintResponse>

    @DELETE("/EmergencyComplaintResponse/{ComplaintId}")
    suspend fun deleteEmergencyComplaint(@Path("ComplaintId") ComplaintId:String)
}