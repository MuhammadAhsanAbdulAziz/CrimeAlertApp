package com.example.crimealert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimealert.models.UserRequest
import com.example.crimealert.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData get() = userRepository.userResponseLiveData
    val statusLiveData get() = userRepository.statusLiveData

    fun getuser() {
        viewModelScope.launch {
            userRepository.getUser()
        }
    }

    fun deleteuser(complaintId: String){
        viewModelScope.launch {
            userRepository.deleteUser(complaintId)
        }
    }

    fun updateuser(userId : String, userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.updateUser(userId,userRequest)
        }
    }
}