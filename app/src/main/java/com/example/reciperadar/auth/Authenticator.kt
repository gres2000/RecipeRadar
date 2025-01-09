package com.example.reciperadar.auth

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reciperadar.api_service.data_classes.LoginData
import com.example.reciperadar.api_service.data_classes.LoginResponseData
import com.example.reciperadar.api_service.data_classes.RegisterData
import com.example.reciperadar.api_service.data_classes.RegisterResponseData
import com.example.reciperadar.api_service.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//cannot use singleton, because this class can only access sharedpreferences when called in an activity
class Authenticator(val activity: AppCompatActivity) {
    val sharedPref = activity.getSharedPreferences("jwt_token", MODE_PRIVATE)!!

    fun login(email: String, password: String) {
        val request = LoginData(email, password)
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<LoginResponseData> {
            override fun onResponse(call: Call<LoginResponseData>, response: Response<LoginResponseData>) {

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()

                    //set token in shared preferences
                    val editor = sharedPref.edit()
                    editor.putString("token", loginResponse!!.token).apply()
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

    fun register(username: String, email: String, password: String) {
        val request = RegisterData(username, email, password)
        RetrofitClient.apiService.registerUser(request).enqueue(object : Callback<RegisterResponseData> {
            override fun onResponse(call: Call<RegisterResponseData>, response: Response<RegisterResponseData>) {

                if (response.isSuccessful) {
                    //val registerResponse = response.body()
                    Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show()

                } else {
                    val registerResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map: Map<String, Any> = gson.fromJson(registerResponse, type)
                    Toast.makeText(activity, "Registration failed: ${map["message"]}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponseData>, t: Throwable) {
                Toast.makeText(activity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //get token from shared preferences
    fun getToken(): String? {
        return sharedPref.getString("token", null)
    }
}