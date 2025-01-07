package com.example.reciperadar.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reciperadar.R
import com.example.reciperadar.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            loginEvent()
        }

        binding.goToRegisterButton.setOnClickListener {

            // When the login button is clicked, replace the current fragment with RegisterFragment
            goToRegisterEvent()
        }
    }
    private fun goToRegisterEvent() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.auth_fragment_container,
                RegisterFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    private fun loginEvent() {
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        // Add your login logic here
        if (validateInput(username, password)) {
            // Proceed to next screen or show success message
        } else {
            // Show error message
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        // Basic validation logic
        return username.isNotEmpty() && password.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
