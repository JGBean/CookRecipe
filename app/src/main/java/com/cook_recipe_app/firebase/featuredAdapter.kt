package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cook_recipe_app.firebase.databinding.FragmentFeaturedrecyclerBinding

class featuredAdapter(
    private var bookmarkedItems: List<featuredItem>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<featuredAdapter.Holder>() {

    class Holder(private val binding: FragmentFeaturedrecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: featuredItem, onClick: (String) -> Unit) {
            binding.featuredName.text = item.item

            // Build the image URL dynamically using menuId
            val imageUrl = "https://firebasestorage.googleapis.com/v0/b/cook-recipe-app.firebasestorage.app/o/image%2F${item.item}.png?alt=media&token=f78a262c-46ee-4528-be20-928883445b5d"

            // Load image using Glide
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_recipe) // Placeholder image
                .error(R.drawable.ic_recipe) // Error image
                .into(binding.featuredImage) // Target ImageView

            binding.root.setOnClickListener { onClick(item.item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentFeaturedrecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(bookmarkedItems[position], onItemClicked)
    }

    override fun getItemCount(): Int {
        return bookmarkedItems.size
    }

    fun updateData(newItems: List<featuredItem>) {
        bookmarkedItems = newItems
        notifyDataSetChanged()
    }
}