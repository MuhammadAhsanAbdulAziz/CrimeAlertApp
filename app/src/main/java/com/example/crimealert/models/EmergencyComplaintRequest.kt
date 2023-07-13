package com.example.crimealert.models

data class EmergencyComplaintRequest(
    val ECompDescription: String,
    val ECompStatus: Int,
    val ECompTitle: String
)