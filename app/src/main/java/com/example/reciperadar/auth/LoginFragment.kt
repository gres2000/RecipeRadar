package com.example.reciperadar.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reciperadar.R
import com.example.reciperadar.app.MainActivity
import com.example.reciperadar.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set email if successful registration
        arguments?.let {
            val value = it.getString("emailEditTextValue")

            binding.emailEditText.setText(value)
        }

        //onClick listeners
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

    private fun goToMainMenuEvent() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loginEvent() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (validateInput(email, password)) {
            val auth = Authenticator(requireActivity() as AppCompatActivity)

            binding.loginButton.isEnabled = false
            binding.goToRegisterButton.isEnabled = false

            auth.login(email, password, object : AuthCallBack {
                override fun onSuccess() {
                    goToMainMenuEvent()
                }

                override fun onError(message: String?) {
                    binding.loginButton.isEnabled = true
                    binding.goToRegisterButton.isEnabled = true
                }
            })
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
