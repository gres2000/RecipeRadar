package com.example.reciperadar.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.reciperadar.R
import com.example.reciperadar.databinding.LoginFragmentBinding
import com.example.reciperadar.databinding.RegisterFragmentBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
            Log.d("button pressed", "one")
        }

        binding.backToRLoginButton.setOnClickListener {
            backToLoginEvent(null)
        }
    }

    private fun backToLoginEvent(bundle: Bundle?) {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragment = LoginFragment()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.auth_fragment_container,
                fragment
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
            auth.register(username, email, password, object : AuthCallBack {
                override fun onSuccess() {
                    val bundle = Bundle()
                    bundle.putString("emailEditTextValue", email)

                    backToLoginEvent(bundle)
                }

                override fun onError(message: String?) {
                    val gson = Gson()
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map: Map<String, Any> = gson.fromJson(message, type)

                    binding.emailEditText.error = map["message"].toString()

                    binding.registerButton.isEnabled = true
                    binding.backToRLoginButton.isEnabled = true
                }
            })
            binding.registerButton.isEnabled = false
            binding.backToRLoginButton.isEnabled = false
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {

        var isValid = true

        // Validate Username
        if (username.isEmpty()) {
            binding.usernameEditText.error = "Username cannot be empty"
            isValid = false
        }

        // Validate Email
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please enter a valid email"
            isValid = false
        }

        // Validate Password
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password cannot be empty"
            isValid = false
        }

        // Validate Confirm Password
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordEditText.error = "Please confirm your password"
            isValid = false
        } else if (confirmPassword != password) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
