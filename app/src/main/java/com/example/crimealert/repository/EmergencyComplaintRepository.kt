package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.EmergencyComplaintApi
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class EmergencyComplaintRepository @Inject constructor(private val emergencyComplaintApi: EmergencyComplaintApi) {

    private val _complaintResponseLiveData = MutableLiveData<NetworkResult<List<EmergencyComplaintResponse>>>()
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val complaintResponseLiveData: LiveData<NetworkResult<List<EmergencyComplaintResponse>>>
        get() = _complaintResponseLiveData
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getallComplaint() {
        _complaintResponseLiveData.postValue(NetworkResult.Loading())
        val response = emergencyComplaintApi.getallEmergencyComplaint()
        handleResponse(response)
    }

    suspend fun deleteComplaint(complaintId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        emergencyComplaintApi.deleteEmergencyComplaint(complaintId)
        _statusLiveData.postValue(NetworkResult.Success("Complaint Deleted"))
    }

    suspend fun updateComplaint(ComplaintId : String, emergencyComplaintRequest: EmergencyComplaintRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = emergencyComplaintApi.updateEmergencyComplaint(ComplaintId,emergencyComplaintRequest)
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("Complaint Updated"))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    private fun handleResponse(response: Response<List<EmergencyComplaintResponse>>) {
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