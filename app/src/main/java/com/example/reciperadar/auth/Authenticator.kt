package com.example.reciperadar.auth

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reciperadar.api_service.data_classes.login.LoginData
import com.example.reciperadar.api_service.data_classes.login.LoginResponseData
import com.example.reciperadar.api_service.data_classes.register.RegisterData
import com.example.reciperadar.api_service.data_classes.register.RegisterResponseData
import com.example.reciperadar.api_service.RetrofitClient
import com.example.reciperadar.api_service.data_classes.token.TokenData
import com.example.reciperadar.api_service.data_classes.token.TokenResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//cannot use singleton, because this class can only access sharedpreferences when called in an activity

interface AuthCallBack {
    fun onSuccess()
    fun onError(message: String?)
}

class Authenticator(val activity: AppCompatActivity) {
    val sharedPref = activity.getSharedPreferences("jwt_token", MODE_PRIVATE)!!

    fun login(email: String, password: String, callback: AuthCallBack) {
        val request = LoginData(email, password)
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<LoginResponseData> {
            override fun onResponse(call: Call<LoginResponseData>, response: Response<LoginResponseData>) {

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()

                    //set token in shared preferences
                    val editor = sharedPref.edit()
                    editor.putString("token", loginResponse!!.token).apply()
                    callback.onSuccess()
                } else {
                    val loginResponse = response.errorBody()?.string()
                    //convert loginResponse to map
                    val gson = Gson()
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map: Map<String, Any> = gson.fromJson(loginResponse, type)
                    Toast.makeText(activity, "Login failed: ${map["message"]}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponseData>, t: Throwable) {
                Toast.makeText(activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun register(name: String, email: String, password: String, callback: AuthCallBack) {
        val request = RegisterData(name, email, password)
        RetrofitClient.apiService.registerUser(request).enqueue(object : Callback<RegisterResponseData> {
            //local response
            override fun onResponse(call: Call<RegisterResponseData>, response: Response<RegisterResponseData>) {

                //server response
                if (response.isSuccessful) {
//                    val registerResponse = response.body().toString()
                    Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    callback.onSuccess()
                } else {
                    val registerResponse = response.errorBody()?.string()
                    callback.onError(registerResponse)
                }
            }

            override fun onFailure(call: Call<RegisterResponseData>, t: Throwable) {
                Toast.makeText(activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                callback.onError("Network Error: ${t.message}")
            }
        })
    }

    fun logout(callback: AuthCallBack) {
        val token = "Bearer " + getToken()
        RetrofitClient.apiService.logoutUser(token).enqueue(object : Callback<TokenResponseData> {
            override fun onResponse(call: Call<TokenResponseData>, response: Response<TokenResponseData>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Logout Successful", Toast.LENGTH_SHORT).show()

                    //delete token from shared preferences
                    val editor = sharedPref.edit()
                    editor.remove("token").apply()

                    callback.onSuccess()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Toast.makeText(activity, errorResponse, Toast.LENGTH_SHORT).show()
                    callback.onError(errorResponse)
                }
            }

            override fun onFailure(call: Call<TokenResponseData>, t: Throwable) {
                Toast.makeText(activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                callback.onError("Network Error: ${t.message}")
            }
        })
    }

    //get token from shared preferences
    fun getToken(): String? {
        val str = sharedPref.getString("token", null)
        return sharedPref.getString("token", null)
    }
}