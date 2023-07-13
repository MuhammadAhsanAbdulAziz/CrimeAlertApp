package com.example.crimealert.models

data class EmergencyComplaintResponse(
    val CreatedAt: String,
    val ECompDescription: String,
    val ECompId: Int,
    val ECompStatus: Int,
    val ECompTitle: String
)