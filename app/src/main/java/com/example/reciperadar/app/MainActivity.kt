package com.example.reciperadar.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reciperadar.R
import com.example.reciperadar.app.ingredients.data_classes.Ingredient
import com.example.reciperadar.app.ingredients.IngredientsFragment
import com.example.reciperadar.app.new_recipe.NewRecipeFragment
import com.example.reciperadar.app.recipes.RecipesFragment
import com.example.reciperadar.app.settings.SettingsFragment
import com.example.reciperadar.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val ingredientList = mutableListOf<Ingredient>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNavigationView

        loadFragment(IngredientsFragment())

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_ingredients -> IngredientsFragment()
                R.id.nav_recipes -> RecipesFragment()
                R.id.nav_new_recipe -> NewRecipeFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> IngredientsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    fun addNewIngredient(ing: Ingredient) {
        ingredientList.add(ing)
    }

    fun notifyAdapter() {
        (supportFragmentManager.findFragmentById(R.id.main_container) as IngredientsFragment).itemAdded(ingredientList.size - 1)
    }

    fun getIngredientList():  MutableList<Ingredient> {
        return ingredientList
    }
}