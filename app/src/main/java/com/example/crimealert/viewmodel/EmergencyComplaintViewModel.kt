package com.example.crimealert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.repository.EmergencyComplaintRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyComplaintViewModel @Inject constructor(private val emergencycomplaintRepository: EmergencyComplaintRepository) : ViewModel() {

    val complaintResponseLiveData get() = emergencycomplaintRepository.complaintResponseLiveData
    val statusLiveData get() = emergencycomplaintRepository.statusLiveData


    fun getallcomplaints() {
        viewModelScope.launch {
            emergencycomplaintRepository.getallComplaint()
        }
    }

    fun deletecomplaint(complaintId: String){
        viewModelScope.launch {
            emergencycomplaintRepository.deleteComplaint(complaintId)
        }
    }

    fun updatecomplaint(complaintId : String, emergencyComplaintRequest: EmergencyComplaintRequest){
        viewModelScope.launch {
            emergencycomplaintRepository.updateComplaint(complaintId,emergencyComplaintRequest)
        }
    }
    fun updatestatuscomplaint(complaintId : String, emergencyComplaintRequest: EmergencyComplaintRequest){
        viewModelScope.launch {
            emergencycomplaintRepository.updateStatusComplaint(complaintId,emergencyComplaintRequest)
        }
    }
}