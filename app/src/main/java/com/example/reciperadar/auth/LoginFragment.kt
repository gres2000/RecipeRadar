package com.example.reciperadar.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        // Add your login logic here
        if (validateInput(email, password)) {
            val auth = Authenticator(requireActivity() as AppCompatActivity)

            auth.login(email, password)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {

        var isValid = true

        // Validate Email
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.emailEditText.error = null
        }

        // Validate Password
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password cannot be empty"
            isValid = false
        } else {
            binding.passwordEditText.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
