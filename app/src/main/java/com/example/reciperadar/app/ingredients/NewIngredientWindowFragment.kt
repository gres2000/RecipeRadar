package com.example.reciperadar.app.ingredients

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.set
import androidx.fragment.app.DialogFragment
import com.example.reciperadar.app.MainActivity
import com.example.reciperadar.databinding.NewIngredientWindowFragmentBinding
import java.time.LocalDate

class NewIngredientWindowFragment : DialogFragment() {
    private var _binding: NewIngredientWindowFragmentBinding? = null
    private val binding get() = _binding!!

    private var editTextHelper = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewIngredientWindowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = binding.closeButton
        closeButton.setOnClickListener {
            dismiss()  // Dismiss the dialog
        }

        val saveButton = binding.saveButton
        saveButton.setOnClickListener {

            if (validateInputs()) {
                var date: LocalDate? = null
                if (binding.switchExpiryButton.isChecked) {
                    date = LocalDate.of(
                        binding.yearEditText.text.toString().toInt(),
                        binding.monthEditText.text.toString().toInt(),
                        binding.dayEditText.text.toString().toInt()
                    )
                }
                val newIngredient = Ingredient(
                    binding.autoCompleteTextView.text.toString(),
                    binding.descriptionEditText.text?.toString(),
                    binding.quantityEditText.text.toString().toFloat(),
                    binding.unitSpinner.selectedItem.toString(),
                    binding.kcalEditText.text?.toString()?.toIntOrNull(),
                    date,
                    binding.sugarEditText.text?.toString()?.toIntOrNull(),
                    binding.fiberEditText.text?.toString()?.toIntOrNull(),
                    binding.proteinEditText.text?.toString()?.toIntOrNull(),
                    binding.fatEditText.text?.toString()?.toIntOrNull(),
                    binding.carbohydrateEditText.text?.toString()?.toIntOrNull()
                )

                (requireActivity() as MainActivity).addNewIngredient(newIngredient)
                dismiss()
            }
        }

        setupExpiry()
        setupAutoCompleteSearchBar()
        setupUnitBar()
        setupExpiryEditText()
        setupDatePickerImageButton()
        setupToggleDetailsButton()
    }

    private fun setupExpiry() {
        binding.switchExpiryButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.yearEditText.isEnabled = true
                binding.monthEditText.isEnabled = true
                binding.dayEditText.isEnabled = true
                binding.datePickerImageButton.isEnabled = true
                binding.datePickerImageButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.yearEditText.isEnabled = false
                binding.monthEditText.isEnabled = false
                binding.dayEditText.isEnabled = false
                binding.datePickerImageButton.isEnabled = false
                binding.datePickerImageButton.setBackgroundColor(Color.parseColor("#787878"))
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (binding.autoCompleteTextView.text.toString().isBlank()) {
            binding.autoCompleteTextView.error = "This field is required"
            isValid = false
        }

        if (binding.quantityEditText.text.toString().isBlank()) {
            binding.quantityEditText.error = "Quantity is required"
            isValid = false
        }


        if(binding.switchExpiryButton.isChecked){
            if (binding.yearEditText.text.toString().isBlank() ||
                binding.monthEditText.text.toString().isBlank() ||
                binding.dayEditText.text.toString().isBlank()
            ) {
                binding.yearEditText.error = "Year is required"
                binding.monthEditText.error = "Month is required"
                binding.dayEditText.error = "Day is required"
                isValid = false
            } else if (
                binding.yearEditText.text.toString().toIntOrNull() == null ||
                binding.monthEditText.text.toString().toIntOrNull() == null ||
                binding.dayEditText.text.toString().toIntOrNull() == null
            ) {
                binding.yearEditText.error = "Enter a valid year"
                binding.monthEditText.error = "Enter a valid month"
                binding.dayEditText.error = "Enter a valid day"
                isValid = false
            }
        }

        return isValid
    }

    private fun setupToggleDetailsButton() {
        val toggleDetailsButton = binding.toggleDetailsButton
        val additionalDetailsLayout = binding.additionalDetailsLayout

        toggleDetailsButton.setBackgroundColor(Color.parseColor("#b3b3b3"))

        toggleDetailsButton.setOnClickListener {
            if (additionalDetailsLayout.visibility == View.GONE) {
                additionalDetailsLayout.visibility = View.VISIBLE
                toggleDetailsButton.setBackgroundColor(Color.parseColor("#636363"))
                toggleDetailsButton.setTextColor(Color.parseColor("#ffffff"))
            } else {
                additionalDetailsLayout.visibility = View.GONE
                toggleDetailsButton.setBackgroundColor(Color.parseColor("#b3b3b3"))
                toggleDetailsButton.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    private fun setupDatePickerImageButton() {
        val datePickerImageButton = binding.datePickerImageButton

        datePickerImageButton.setOnClickListener {
            val datePicker = DatePickerDialog { year, month, day ->
                val properMonth = month + 1
                binding.yearEditText.setText(year.toString())
                binding.monthEditText.setText(properMonth.toString())
                binding.dayEditText.setText(day.toString())
            }
            datePicker.show(parentFragmentManager, "datePicker")
        }
    }

    private fun setupExpiryEditText() {
        val et1 = binding.yearEditText
        val et2 = binding.monthEditText
        val et3 = binding.dayEditText

        // year
        et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //pass
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //pass
            }

            // move cursor
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 4) {
                    et2.requestFocus()
                }
            }
        })

        // month
        et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //pass
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && s.toString().toInt() > 12) {
                    et2.setText("12")
                }
            }

            // move cursor
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 2) {
                    et3.requestFocus()
                }
            }
        })

        et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //pass
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && s.toString().toInt() > 31) {
                    et3.setText("31")
                    et3.setSelection(et3.length())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //pass
            }
        })


        et2.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_DEL && et2.text.isEmpty() && editTextHelper) {
                    et1.requestFocus()
                    et1.setSelection(et1.length())
                    // delete the last character from previous edittext
                    et1.dispatchKeyEvent(
                        KeyEvent(
                            0, 0, KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DEL, 0
                        )
                    )
                    et1.dispatchKeyEvent(
                        KeyEvent(
                            0, 0, KeyEvent.ACTION_UP,
                            KeyEvent.KEYCODE_DEL, 0
                        )
                    )
                }

                return false
            }
        })

        et3.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_DEL && et3.text.isEmpty()) {
                    et2.requestFocus()
                    et2.setSelection(et2.length())
                    editTextHelper = false
                    // delete the last character from previous edittext
                    et2.dispatchKeyEvent(
                        KeyEvent(
                            0, 0, KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DEL, 0
                        )
                    )
                    et2.dispatchKeyEvent(
                        KeyEvent(
                            0, 0, KeyEvent.ACTION_UP,
                            KeyEvent.KEYCODE_DEL, 0
                        )
                    )
                    editTextHelper = true
                }

                return false
            }
        })
    }

    private fun setupAutoCompleteSearchBar() {
        val predefinedValues = listOf(
            "Alma",
            "Banán",
            "Cseresznye",
            "Datolya",
            "Szőlő",
            "Narancs",
            "Fekete Alma Paprika"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            predefinedValues
        )

        val autoCompleteTextView = binding.autoCompleteTextView

        autoCompleteTextView.setAdapter(adapter)

        //minimum letters before it suggests something
        autoCompleteTextView.threshold = 1
    }

    private fun setupUnitBar() {
        val predefinedValues = listOf("g", "dkg", "kg", "ml", "dl", "l")

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, predefinedValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = binding.unitSpinner
        spinner.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            // Set the dialog to be full screen
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}