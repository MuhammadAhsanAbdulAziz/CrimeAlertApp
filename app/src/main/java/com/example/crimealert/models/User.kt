package com.example.crimealert.models

data class User(
    val CreatedAt: String,
    val Email: String,
    val FName: String,
    val LName: String,
    val Password: String,
    val RoleId: Int,
    val UserId: Int
)