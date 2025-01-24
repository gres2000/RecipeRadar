package com.example.reciperadar.api_service.data_classes.register

data class RegisterResponseData (
    val message: String,
    val errors: RegisterErrorsData?
)