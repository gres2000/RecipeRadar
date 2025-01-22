package com.example.reciperadar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.reciperadar.R
import com.example.reciperadar.app.ingredients.IngredientsFragment
import com.example.reciperadar.app.new_recipe.NewRecipeFragment
import com.example.reciperadar.app.recipes.RecipesFragment
import com.example.reciperadar.app.settings.SettingsFragment
import com.example.reciperadar.databinding.MainActivityBinding
import com.example.reciperadar.ui.theme.RecipeRadarTheme

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
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

}