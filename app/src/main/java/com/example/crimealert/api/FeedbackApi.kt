package com.example.crimealert.api

import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FeedbackApi {

    @GET("/feedbacks/all")
    suspend fun getallFeedback() : Response<List<FeedbackResponse>>

    @POST("/feedbacks")
    suspend fun addFeedback(@Body feedbackRequest: FeedbackRequest) : Response<FeedbackResponse>

    @PUT("/feedbacks/{feedbackId}")
    suspend fun updateFeedback(@Path("feedbackId") feedbackId:String, @Body feedbackRequest: FeedbackRequest) : Response<FeedbackResponse>

    @DELETE("/feedbacks/{feedbackId}")
    suspend fun deleteFeedback(@Path("feedbackId") feedbackId:String)
}