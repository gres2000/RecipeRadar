package com.example.reciperadar.api_service

import com.example.reciperadar.api_service.data_classes.LoginData
import com.example.reciperadar.api_service.data_classes.LoginResponseData
import com.example.reciperadar.api_service.data_classes.RegisterData
import com.example.reciperadar.api_service.data_classes.RegisterResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun registerUser(@Body user: RegisterData): Call<RegisterResponseData>

    @POST("login")
    fun loginUser(@Body user: LoginData): Call<LoginResponseData>
}