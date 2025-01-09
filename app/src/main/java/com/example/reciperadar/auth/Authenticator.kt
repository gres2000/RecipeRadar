package com.example.reciperadar.auth

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatActivity

class Authenticator(activity: AppCompatActivity) {
    val sharedPref = activity.getSharedPreferences("jwt_token", MODE_PRIVATE)

    fun login(username: String, password: String) {

    }

    fun register(username: String, email: String, password: String) {

    }

    fun getToken() : String? {
        return null
    }
}