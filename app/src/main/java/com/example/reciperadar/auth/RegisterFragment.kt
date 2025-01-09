package com.example.reciperadar.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.reciperadar.R
import com.example.reciperadar.databinding.LoginFragmentBinding
import com.example.reciperadar.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            registerEvent()
        }

        binding.backToRLoginButton.setOnClickListener {
            backToLoginEvent()
        }
    }

    private fun backToLoginEvent() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.auth_fragment_container,
                LoginFragment()
            )
            .commit()
    }

    private fun registerEvent() {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        // Add your login logic here
        if (validateInput(username, email, password, confirmPassword)) {
            val auth = Authenticator(requireActivity() as AppCompatActivity)

            auth.register(username, email, password)
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {

        var isValid = true

        // Validate Username
        if (username.isEmpty()) {
            binding.usernameEditText.error = "Username cannot be empty"
            isValid = false
        } else {
            binding.usernameEditText.error = null
        }

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

        // Validate Confirm Password
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordEditText.error = "Please confirm your password"
            isValid = false
        } else if (confirmPassword != password) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            isValid = false
        } else {
            binding.confirmPasswordEditText.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
