package com.example.crimealert.utils

import android.content.Context
import com.example.crimealert.models.UserResponse
import com.example.crimealert.utils.Constants.PREFS_TOKEN_FILE
import com.example.crimealert.utils.Constants.USER_INFO
import com.example.crimealert.utils.Constants.USER_TOKEN
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UtilManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token : String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }
    fun saveUser(user: UserResponse){
        val editor = prefs.edit()
        editor.putString(USER_INFO, Gson().toJson(user).toString())
        editor.apply()
    }

    fun getToken() : String?{
        return prefs.getString(USER_TOKEN,null)
    }
    fun getUser() : String? {
        return prefs.getString(USER_INFO,null)
    }
}