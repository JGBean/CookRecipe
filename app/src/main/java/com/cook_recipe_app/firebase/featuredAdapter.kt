package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.databinding.FragmentFeaturedrecyclerBinding

class featuredAdapter(
    private var bookmarkedItems: List<featuredItem>,
    private val onItemClicked: (String) -> Unit // 클릭 이벤트 콜백
) : RecyclerView.Adapter<featuredAdapter.Holder>() {

    class Holder(private val binding: FragmentFeaturedrecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: featuredItem, onClick: (String) -> Unit) {
            binding.featuredName.text = item.item
            binding.root.setOnClickListener { onClick(item.item) } // 클릭 이벤트 전달
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