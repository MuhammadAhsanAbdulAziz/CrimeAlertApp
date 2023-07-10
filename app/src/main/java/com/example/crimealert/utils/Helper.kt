package com.example.crimealert.utils

import android.text.TextUtils
import android.util.Patterns

object Helper {
    fun validateCredentials(username : String,email : String, password : String, isLogin : Boolean) : Pair<Boolean,String>{
        var result = Pair(true,"")
        if(!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            result = Pair(false,"Please provide the credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide correct email")
        }
        else if(password.length <= 6){
            result = Pair(false,"Password length should be greater than 5")
        }
        return result
    }
    fun validateRegisterCredentials(fname : String,lname : String,email : String, password : String) : Pair<Boolean,String>{
        var result = Pair(true,"")
        if(TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            result = Pair(false,"Please provide the credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide correct email")
        }
        else if(password.length <= 6){
            result = Pair(false,"Password length should be greater than 5")
        }
        return result
    }

}