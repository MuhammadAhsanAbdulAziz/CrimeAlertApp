package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.ComplaintApi
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ComplaintRepository @Inject constructor(private val complaintApi: ComplaintApi) {

    private val _complaintResponseLiveData = MutableLiveData<NetworkResult<List<ComplaintResponse>>>()
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val complaintResponseLiveData: LiveData<NetworkResult<List<ComplaintResponse>>>
        get() = _complaintResponseLiveData
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getComplaint() {
        _complaintResponseLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.getComplaint()
        handleResponse(response)
    }

    suspend fun getPendingComplaint() {
        _complaintResponseLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.getPendingComplaint()
        handleResponse(response)
    }

    suspend fun getCompletedComplaint() {
        _complaintResponseLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.getCompletedComplaint()
        handleResponse(response)
    }

    suspend fun getallComplaint() {
        _complaintResponseLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.getallComplaint()
        handleResponse(response)
    }

    suspend fun createComplaint(complaintRequest: ComplaintRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.addComplaint(complaintRequest)
        handleResponse(response,"Complaint Created")

    }

    suspend fun deleteComplaint(complaintId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.deleteComplaint(complaintId)
        _statusLiveData.postValue(NetworkResult.Success("Complaint Deleted"))
    }

    suspend fun updateComplaint(ComplaintId : String, complaintRequest: ComplaintRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = complaintApi.updateComplaint(ComplaintId,complaintRequest)
        handleResponse(response,"Complaint Updated")
    }

    private fun handleResponse(response: Response<ComplaintResponse>,message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    private fun handleResponse(response: Response<List<ComplaintResponse>>) {
        if (response.isSuccessful && response.body() != null) {
            _complaintResponseLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _complaintResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _complaintResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}