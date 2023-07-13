package com.example.crimealert.models

data class ComplaintRequest(
    val CompDescription: String,
    val CompTitle: String,
    val CompUPhone: String,
    val CompStatus : Int
)