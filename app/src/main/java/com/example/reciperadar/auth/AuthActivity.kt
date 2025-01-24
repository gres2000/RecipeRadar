package com.example.reciperadar.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reciperadar.R
import com.example.reciperadar.app.MainActivity
import com.example.reciperadar.databinding.AuthActivityBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("jwt_token", MODE_PRIVATE)
        val token = sharedPrefs.getString("token", null)
        if (token != null) {
            // if the user is logged in start the main activity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {

            if (savedInstanceState == null) {
                val fragment = LoginFragment()


                supportFragmentManager.beginTransaction()
                    .replace(R.id.auth_fragment_container, fragment)
                    .commit()
            }
        }
    }
}