package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.databinding.FragmentMywritingrecyclerBinding

class mywritingAdapter(
    private var writedItems: List<mywritingItem>,
    private val onItemClicked: (mywritingItem) -> Unit // 클릭 이벤트 콜백
) : RecyclerView.Adapter<mywritingAdapter.Holder>() {

    class Holder(private val binding: FragmentMywritingrecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: mywritingItem, onClick: (mywritingItem) -> Unit) {
            binding.mywritingTitle.text = item.item
            binding.root.setOnClickListener { onClick(item) } // 클릭 이벤트에서 item 전체를 전달
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentMywritingrecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(writedItems[position], onItemClicked)
    }

    override fun getItemCount(): Int {
        return writedItems.size
    }

    fun updateData(newItems: List<mywritingItem>) {
        writedItems = newItems
        notifyDataSetChanged()
    }
}