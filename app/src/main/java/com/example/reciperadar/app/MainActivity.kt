package com.example.reciperadar.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reciperadar.R
import com.example.reciperadar.api_service.RetrofitClient
import com.example.reciperadar.api_service.data_classes.ingredients.IngredientsListData
import com.example.reciperadar.api_service.data_classes.ingredients.IngredientsListResponseData
import com.example.reciperadar.api_service.data_classes.register.RegisterResponseData
import com.example.reciperadar.api_service.data_classes.token.TokenData
import com.example.reciperadar.app.ingredients.data_classes.Ingredient
import com.example.reciperadar.app.ingredients.IngredientsFragment
import com.example.reciperadar.app.new_recipe.NewRecipeFragment
import com.example.reciperadar.app.recipes.RecipesFragment
import com.example.reciperadar.app.settings.SettingsFragment
import com.example.reciperadar.auth.AuthActivity
import com.example.reciperadar.auth.AuthCallBack
import com.example.reciperadar.auth.Authenticator
import com.example.reciperadar.databinding.MainActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface IngredientsCallBack {
    fun onSuccess()
    fun onError(message: String?)
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    var dataArrived = false

    private val ingredientList = mutableListOf<Ingredient>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val profileImageButton = binding.profileButton

        val fragment = IngredientsFragment()
        loadFragment(fragment)

        //read ingredients from internal memory
        CoroutineScope(Dispatchers.IO).launch {
            dataArrived = false
            val auth = Authenticator(this@MainActivity)
            val userId = auth.getUserId()
            ingredientList.addAll(readListAsync("{$userId}ingredients"))
            dataArrived = true
        }


        setupBottomNavigationView(bottomNavigationView)
        setupProfileButton(profileImageButton)
    }

    private fun setupProfileButton(profileButton: ImageButton) {
        profileButton.setOnClickListener{
            val popupMenu = PopupMenu(this, profileButton)

            menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.menu_item_1 -> {
                        // Handle click for menu item 1
                        val auth = Authenticator(this@MainActivity)
                        auth.logout(object : AuthCallBack {
                            override fun onSuccess() {

                                goToAuthActivity()
                            }

                            override fun onError(message: String?) {
                                //pass
                            }
                        })
                        true
                    }
                    else -> false
                }

            }
            popupMenu.show()
        }

    }

    private fun goToAuthActivity() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this@MainActivity.finish()
    }

    private fun setupBottomNavigationView(bottomNavigationView: BottomNavigationView) {
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
            .commitNow()
    }

    fun addNewIngredient(ing: Ingredient, userId: String?) {
        ingredientList.add(ing)
        saveListAsync("{$userId}ingredients", ingredientList)
    }

    fun notifyAdapter() {
        (supportFragmentManager.findFragmentById(R.id.main_container) as IngredientsFragment).itemAdded(ingredientList.size - 1)
    }

    fun getIngredientList(): MutableList<Ingredient> {
        return ingredientList
    }

    private fun saveListAsync(fileName: String, list: MutableList<Ingredient>) {
        CoroutineScope(Dispatchers.IO).launch {
            val json = Gson().toJson(list)

            openFileOutput(fileName, MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
        }
    }

    private suspend fun readListAsync(fileName: String): List<Ingredient> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = openFileInput(fileName).bufferedReader().use { it.readText() }
                Gson().fromJson(jsonString, object : TypeToken<List<Ingredient>>() {}.type)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun sendIngredientsRequest(callback: AuthCallBack) {
        val request = IngredientsListData(ingredientList)
        val auth = Authenticator(this@MainActivity)
        val token = "Bearer " + auth.getToken()
        RetrofitClient.apiService.uploadIngredients(token, request).enqueue(object : Callback<IngredientsListResponseData> {
            //local response
            override fun onResponse(call: Call<IngredientsListResponseData>, response: Response<IngredientsListResponseData>) {

                //server response
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "upload successful", Toast.LENGTH_SHORT).show()
                    callback.onSuccess()
                } else {
                    val ingredientsResponse = response.errorBody()?.string()
                    Toast.makeText(this@MainActivity, ingredientsResponse, Toast.LENGTH_SHORT).show()
                    callback.onError(ingredientsResponse)
                }
            }

            override fun onFailure(call: Call<IngredientsListResponseData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                callback.onError("Network Error: ${t.message}")
            }
        })
    }

    fun uploadIngredients() {
        sendIngredientsRequest(object : AuthCallBack {
            override fun onSuccess() {
                //pass
            }

            override fun onError(message: String?) {
                //pass
            }
        })
    }
}