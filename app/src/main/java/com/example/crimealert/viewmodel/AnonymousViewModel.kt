package com.example.crimealert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.repository.AnonymousRepository
import com.example.crimealert.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnonymousViewModel @Inject constructor(private val anonymousRepository: AnonymousRepository) : ViewModel() {

    val feedbackResponseLiveData get() = anonymousRepository.feedbackResponseLiveData
    val ecompResponseLiveData get() = anonymousRepository.ecompResponseLiveData
    val statusLiveData get() = anonymousRepository.statusLiveData


    fun createfeedback(feedbackRequest: FeedbackRequest){
        viewModelScope.launch {
            anonymousRepository.createfeedback(feedbackRequest)
        }

    }
    fun addEmergencyComplaint(emergencyComplaintRequest: EmergencyComplaintRequest){
        viewModelScope.launch {
            anonymousRepository.addEmergencyComplaint(emergencyComplaintRequest)
        }

    }
}