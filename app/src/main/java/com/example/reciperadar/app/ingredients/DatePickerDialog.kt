package com.example.reciperadar.app.ingredients

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerDialog(private val onDateSet: (year: Int, month: Int, day: Int) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            onDateSet(selectedYear, selectedMonth, selectedDay)
        }, year, month, day)

        dialog.datePicker.minDate = calendar.timeInMillis

        return dialog
    }
}