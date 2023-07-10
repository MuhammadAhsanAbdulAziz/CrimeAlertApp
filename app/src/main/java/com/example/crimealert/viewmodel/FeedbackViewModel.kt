package com.example.crimealert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(private val feedbackRepository: FeedbackRepository) : ViewModel() {

    val feedbackResponseLiveData get() = feedbackRepository.feedbackResponseLiveData
    val statusLiveData get() = feedbackRepository.statusLiveData

    fun getfeedbacks() {
        viewModelScope.launch {
            feedbackRepository.getFeedback()
        }
    }

    fun getallfeedbacks() {
        viewModelScope.launch {
            feedbackRepository.getallFeedback()
        }
    }

    fun createfeedback(feedbackRequest: FeedbackRequest){
        viewModelScope.launch {
            feedbackRepository.createfeedback(feedbackRequest)
        }

    }

    fun deletefeedback(feedbackId: String){
        viewModelScope.launch {
            feedbackRepository.deletefeedback(feedbackId)
        }
    }

    fun updatefeedback(feedbackId : String, feedbackRequest: FeedbackRequest){
        viewModelScope.launch {
            feedbackRepository.updatefeedback(feedbackId,feedbackRequest)
        }
    }
}