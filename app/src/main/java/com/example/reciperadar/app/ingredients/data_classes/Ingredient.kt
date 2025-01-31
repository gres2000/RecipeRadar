package com.example.reciperadar.app.ingredients.data_classes

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Ingredient(
    @SerializedName("ingredient_type_id")val id: Int,
    val name: String,
    val description: String?,
    val quantity: Float,
    val unit: String,
    val kcal: Int?,
    val expiry: LocalDate?,
    val sugar: Int?,
    val fiber: Int?,
    val protein: Int?,
    val fat: Int?,
    val carbohydrate: Int?
)