package com.example.reciperadar.app.ingredients

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciperadar.R
import com.example.reciperadar.app.MainActivity
import com.example.reciperadar.app.ingredients.data_classes.Ingredient
import com.example.reciperadar.databinding.IngredientsFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

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

        startIngredientsLoading()
        setupNewIngredientButton(newIngredientButton)

        setupRecyclerView()
    }

    private fun setupNewIngredientButton(newIngredientButton: Button) {
        newIngredientButton.setOnClickListener {
            val overlayFragment = NewIngredientWindowFragment()

            // Start a fragment transaction to show the fragment
            parentFragmentManager.beginTransaction()
                .add(
                    R.id.main_container,
                    overlayFragment
                )  //add() does not destroy previous fragment
                .addToBackStack(null)
                .setCustomAnimations(
                    R.anim.fragment_slide_in,
                    R.anim.fragment_slide_out
                )
                .commit()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.ingredientsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val ingredients = (requireActivity() as MainActivity).getIngredientList()
        recyclerView.adapter = IngredientItemAdapter(ingredients)
    }

    private fun updateIngredient(
        ingredient: Ingredient,
        name: String? = null,
        description: String? = null,
        quantity: Float? = null,
        unit: String? = null,
        expiry: LocalDate? = null,
        kcal: Int? = null
    ): Ingredient {
        return ingredient.copy(
            name = name ?: ingredient.name,
            description = description ?: ingredient.description,
            quantity = quantity ?: ingredient.quantity,
            unit = unit ?: ingredient.unit,
            expiry = expiry ?: ingredient.expiry,
            kcal = kcal ?: ingredient.kcal
        )
    }

    fun itemAdded(position: Int) {
        binding.ingredientsRecyclerView.adapter?.notifyItemInserted(position)

    }

    private fun startIngredientsLoading() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val ingredientsList = (requireActivity() as MainActivity).getIngredientList()
                if ((requireActivity() as MainActivity).dataArrived) {
                    // Ingredients are loaded, proceed with the next task
                    proceedWithLoadedIngredients(ingredientsList)
                } else {
                    // Continue checking every 200 milliseconds if not loaded yet
                    handler.postDelayed(this, 200)
                }
            }
        }
        handler.post(runnable)
    }

    private fun proceedWithLoadedIngredients(ingredientsList: MutableList<Ingredient>) {
        val recyclerView = binding.ingredientsRecyclerView
        recyclerView.adapter = IngredientItemAdapter(ingredientsList)
        (recyclerView.adapter as IngredientItemAdapter).notifyDataSetChanged()
    }
}