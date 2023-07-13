package com.example.crimealert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.repository.ComplaintRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(private val complaintRepository: ComplaintRepository) : ViewModel() {

    val complaintResponseLiveData get() = complaintRepository.complaintResponseLiveData
    val statusLiveData get() = complaintRepository.statusLiveData

    fun getcomplaints() {
        viewModelScope.launch {
            complaintRepository.getComplaint()
        }
    }

    fun getPendingcomplaints() {
        viewModelScope.launch {
            complaintRepository.getPendingComplaint()
        }
    }

    fun getCompletedcomplaints() {
        viewModelScope.launch {
            complaintRepository.getCompletedComplaint()
        }
    }

    fun getallcomplaints() {
        viewModelScope.launch {
            complaintRepository.getallComplaint()
        }
    }

    fun createcomplaint(complaintRequest: ComplaintRequest){
        viewModelScope.launch {
            complaintRepository.createComplaint(complaintRequest)
        }

    }

    fun deletecomplaint(complaintId: String){
        viewModelScope.launch {
            complaintRepository.deleteComplaint(complaintId)
        }
    }

    fun updatecomplaint(complaintId : String, complaintRequest: ComplaintRequest){
        viewModelScope.launch {
            complaintRepository.updateComplaint(complaintId,complaintRequest)
        }
    }
}