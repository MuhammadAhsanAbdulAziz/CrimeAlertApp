package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.AnonymousApi
import com.example.crimealert.api.FeedbackApi
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import com.example.crimealert.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AnonymousRepository @Inject constructor(private val anonymousApi: AnonymousApi) {

    private val _feedbackResponseLiveData = MutableLiveData<NetworkResult<List<FeedbackResponse>>>()
    private val _ecompResponseLiveData = MutableLiveData<NetworkResult<List<EmergencyComplaintResponse>>>()
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val feedbackResponseLiveData: LiveData<NetworkResult<List<FeedbackResponse>>>
        get() = _feedbackResponseLiveData
    val ecompResponseLiveData: LiveData<NetworkResult<List<EmergencyComplaintResponse>>>
        get() = _ecompResponseLiveData
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun createfeedback(feedbackRequest: FeedbackRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = anonymousApi.addFeedback(feedbackRequest)
        handleResponse(response,"feedback Created")

    }
    suspend fun addEmergencyComplaint(emergencycomplaintRequest: EmergencyComplaintRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = anonymousApi.addEmergencyComplaint(emergencycomplaintRequest)
        handleResponse2(response,"Complaint Created")

    }

    private fun handleResponse(response: Response<FeedbackResponse>,message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }


    private fun handleResponse2(response: Response<EmergencyComplaintResponse>, message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

}