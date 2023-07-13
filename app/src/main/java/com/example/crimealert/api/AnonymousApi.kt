package com.example.crimealert.api

import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AnonymousApi {

    @POST("/feedbacks/anonymous")
    suspend fun addFeedback(@Body feedbackRequest: FeedbackRequest) : Response<FeedbackResponse>

    @POST("/emergencycomplaints")
    suspend fun addEmergencyComplaint(@Body emergencycomplaintRequest: EmergencyComplaintRequest) : Response<EmergencyComplaintResponse>

}