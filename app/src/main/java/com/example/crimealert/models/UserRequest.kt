package com.example.crimealert.models

data class UserRequest(
    val FName: String,
    val LName: String,
    val Username : String,
    val CNIC : String,
    val Email: String,
    val Password: String,
    val RoleId: Int
)