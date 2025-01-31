package com.example.reciperadar.app.ingredients.data_classes

import com.google.gson.annotations.SerializedName

//will need to modify this, when pagination is implemented serverside
data class PaginatedResponseData<T>(
    @SerializedName("data") val data: List<T>
)