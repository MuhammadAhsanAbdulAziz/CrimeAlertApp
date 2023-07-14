package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.AnonymousApi
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import com.example.crimealert.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class AnonymousRepository @Inject constructor(private val anonymousApi: AnonymousApi) {

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun createfeedback(feedbackRequest: FeedbackRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = anonymousApi.addFeedback(feedbackRequest)
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("feedback Created"))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }

    }
    suspend fun addEmergencyComplaint(emergencycomplaintRequest: EmergencyComplaintRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = anonymousApi.addEmergencyComplaint(emergencycomplaintRequest)
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("Emergency Complaint Created"))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }

    }
}