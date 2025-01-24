package com.example.reciperadar.api_service

import com.example.reciperadar.api_service.data_classes.login.LoginData
import com.example.reciperadar.api_service.data_classes.login.LoginResponseData
import com.example.reciperadar.api_service.data_classes.register.RegisterData
import com.example.reciperadar.api_service.data_classes.register.RegisterResponseData
import com.example.reciperadar.api_service.data_classes.token.TokenData
import com.example.reciperadar.api_service.data_classes.token.TokenResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(@Body user: RegisterData): Call<RegisterResponseData>

    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(@Body user: LoginData): Call<LoginResponseData>

    @POST("logout")
    fun logoutUser(@Body token: TokenData): Call<TokenResponseData>
}