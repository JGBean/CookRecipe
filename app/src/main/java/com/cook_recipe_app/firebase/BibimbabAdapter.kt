package com.cook_recipe_app.firebase


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.R
import com.cook_recipe_app.firebase.databinding.IngredientListBinding

class BibimbabAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<BibimbabAdapter.BibimbabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibimbabViewHolder {
        val binding = IngredientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BibimbabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BibimbabViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class BibimbabViewHolder(private val binding: IngredientListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.itemTextView.text = item
        }
    }
}


