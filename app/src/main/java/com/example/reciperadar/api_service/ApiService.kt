package com.example.reciperadar.api_service

import com.example.reciperadar.api_service.data_classes.ingredients.IngredientsListData
import com.example.reciperadar.api_service.data_classes.ingredients.IngredientsListResponseData
import com.example.reciperadar.api_service.data_classes.login.LoginData
import com.example.reciperadar.api_service.data_classes.login.LoginResponseData
import com.example.reciperadar.api_service.data_classes.register.RegisterData
import com.example.reciperadar.api_service.data_classes.register.RegisterResponseData
import com.example.reciperadar.api_service.data_classes.token.TokenData
import com.example.reciperadar.api_service.data_classes.token.TokenResponseData
import com.example.reciperadar.app.ingredients.data_classes.Ingredient
import com.example.reciperadar.app.ingredients.data_classes.IngredientResponseData
import com.example.reciperadar.app.ingredients.data_classes.PaginatedResponseData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //authentication
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(@Body user: RegisterData): Call<RegisterResponseData>

    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(@Body user: LoginData): Call<LoginResponseData>

    @Headers("Accept: application/json")
    @POST("logout")
    fun logoutUser(@Header("Authorization") token: String?): Call<TokenResponseData>

    //ingredients
    @Headers("Accept: application/json")
    @POST("ingredients")
    fun uploadIngredients(
        @Header("Authorization") token: String?,
        @Body user: IngredientsListData
    ): Call<ResponseBody>

    @Headers("Accept: application/json")
    @GET("ingredient-types")
    fun searchIngredients(
        @Header("Authorization") token: String?,
        @Query("search") search: String,
        @Query("page") page: Int = 0
    ): Call<PaginatedResponseData<IngredientResponseData>>
}