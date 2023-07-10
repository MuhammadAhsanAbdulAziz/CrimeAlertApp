package com.example.crimealert.models

data class ComplaintResponse(
    val CompDescription: String,
    val CompId: Int,
    val CompStatus: Int,
    val CompTitle: String,
    val CompUName: String,
    val CompUPhone: String,
    val CreatedAt: String,
    val CreatedBy: Int
)