package com.example.crimealert.models

data class FeedbackResponse(
    val CreatedAt: String,
    val CreatedBy: Int,
    val FeedbackDescription: String,
    val FeedbackRating : Int,
    val FeedbackId: Int
)