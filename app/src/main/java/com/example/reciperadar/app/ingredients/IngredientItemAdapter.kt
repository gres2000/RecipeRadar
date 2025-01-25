package com.example.reciperadar.app.ingredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reciperadar.R
import com.example.reciperadar.app.ingredients.data_classes.Ingredient

class IngredientItemAdapter(private val itemList: List<Ingredient>) : RecyclerView.Adapter<IngredientItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val unitTextView: TextView = itemView.findViewById(R.id.unitTextView)
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_recycler_item, parent, false)
        return ItemViewHolder(view)
    }

    // Bind data to the views
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.titleTextView.text = currentItem.name
        holder.quantityTextView.text = currentItem.quantity.toString()
        holder.unitTextView.text = currentItem.unit
    }

    // Return the total number of items
    override fun getItemCount(): Int = itemList.size
}