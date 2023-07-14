package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.FeedbackApi
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import com.example.crimealert.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class FeedbackRepository @Inject constructor(private val feedbackApi: FeedbackApi) {

    private val _feedbackResponseLiveData = MutableLiveData<NetworkResult<List<FeedbackResponse>>>()
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val feedbackResponseLiveData: LiveData<NetworkResult<List<FeedbackResponse>>>
        get() = _feedbackResponseLiveData
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getallFeedback() {
        _feedbackResponseLiveData.postValue(NetworkResult.Loading())
        val response = feedbackApi.getallFeedback()
        handleResponse(response)
    }

    suspend fun createfeedback(feedbackRequest: FeedbackRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = feedbackApi.addFeedback(feedbackRequest)
        handleResponse(response,"feedback Created")

    }

    suspend fun deletefeedback(feedbackId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        feedbackApi.deleteFeedback(feedbackId)
        _statusLiveData.postValue(NetworkResult.Success("feedback Deleted"))
    }

    suspend fun updatefeedback(feedbackId : String, feedbackRequest: FeedbackRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = feedbackApi.updateFeedback(feedbackId,feedbackRequest)
        handleResponse(response,"feedback Updated")
    }

    private fun handleResponse(response: Response<FeedbackResponse>,message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    private fun handleResponse(response: Response<List<FeedbackResponse>>) {
        if (response.isSuccessful && response.body() != null) {
            _feedbackResponseLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _feedbackResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _feedbackResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}