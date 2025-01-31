package com.example.reciperadar.app.ingredients.data_classes

data class IngredientResponseData(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
