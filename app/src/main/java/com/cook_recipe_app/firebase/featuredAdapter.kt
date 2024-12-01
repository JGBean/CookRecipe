package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.databinding.FragmentFeaturedrecyclerBinding

class featuredAdapter(private var bookmarkedItems: List<featuredItem>) :
    RecyclerView.Adapter<featuredAdapter.Holder>() {

    class Holder(private val binding: FragmentFeaturedrecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: featuredItem) {
            binding.featuredName.text = item.item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentFeaturedrecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(bookmarkedItems[position])
    }

    override fun getItemCount(): Int {
        return bookmarkedItems.size
    }

    // 데이터 갱신 메서드
    fun updateData(newItems: List<featuredItem>) {
        bookmarkedItems = newItems
        notifyDataSetChanged()
    }
}