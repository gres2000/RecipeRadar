package com.example.reciperadar.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        // Add your login logic here
        if (validateInput(username, password, confirmPassword)) {
            // Proceed to next screen or show success message
        } else {
            // Show error message
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        // Basic validation logic
        return username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
