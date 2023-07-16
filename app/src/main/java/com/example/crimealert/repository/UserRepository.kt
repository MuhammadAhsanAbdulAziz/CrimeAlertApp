package com.example.crimealert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crimealert.api.UserApi
import com.example.crimealert.models.User
import com.example.crimealert.models.UserRequest
import com.example.crimealert.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<User>>()
    private val _alluserResponseLiveData = MutableLiveData<NetworkResult<List<User>>>()
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()

    val userResponseLiveData: LiveData<NetworkResult<User>>
        get() = _userResponseLiveData
    val alluserResponseLiveData: LiveData<NetworkResult<List<User>>>
        get() = _alluserResponseLiveData
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getUser() {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.getUser()
        handleResponse(response)
    }
    suspend fun getAllUser() {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.getAllUser()
        if (response.isSuccessful && response.body() != null) {
            _alluserResponseLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


    suspend fun deleteUser(userId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = userApi.deleteUser(userId)
        _statusLiveData.postValue(NetworkResult.Success("User Deleted"))
    }

    suspend fun updateUser(userId : String, userRequest: UserRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = userApi.updateUser(userId,userRequest)
        handleResponse(response,"User Updated")
    }

    suspend fun updateUserRole(userId : String, userRequest: UserRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = userApi.updateUserRole(userId,userRequest)
        handleResponse(response,"User Updated")
    }

    private fun handleResponse(response: Response<User>,message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    private fun handleResponse(response: Response<User>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}