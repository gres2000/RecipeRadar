package com.example.reciperadar.app.ingredients

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reciperadar.databinding.IngredientsFragmentBinding
import com.example.reciperadar.databinding.LoginFragmentBinding

class IngredientsFragment : Fragment() {
    private var _binding: IngredientsFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IngredientsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val newIngredientButton = binding.newIngredientButton


    }
}