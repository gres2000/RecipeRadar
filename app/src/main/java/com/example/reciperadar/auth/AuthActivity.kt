package com.example.reciperadar.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reciperadar.R
import com.example.reciperadar.databinding.AuthActivityBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = LoginFragment()


            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, fragment)
                .commit()
        }


    }
}