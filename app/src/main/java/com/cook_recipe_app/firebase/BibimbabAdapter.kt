package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cook_recipe_app.firebase.databinding.IngredientListBinding
import com.cook_recipe_app.firebase.databinding.MenuImageItemBinding

class BibimbabAdapter(
    private val imageUrl: String, // 요리 이미지 URL
    private val items: List<IngredientItem> // 재료 리스트
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 0
        private const val VIEW_TYPE_INGREDIENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_IMAGE else VIEW_TYPE_INGREDIENT
    }

    override fun getItemCount(): Int {
        return items.size + 1 // 이미지 1개 + 재료 리스트 크기
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val binding = MenuImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(binding)
            }
            VIEW_TYPE_INGREDIENT -> {
                val binding = IngredientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BibimbabViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && holder is ImageViewHolder) {
            holder.bind(imageUrl) // 첫 번째 아이템에 이미지 URL 바인딩
        } else if (position > 0 && holder is BibimbabViewHolder) {
            val item = items[position - 1] // 첫 번째 아이템이 이미지이므로 인덱스 보정
            holder.bind(item)
        }
    }

    // 요리 이미지 ViewHolder
    inner class ImageViewHolder(private val binding: MenuImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // 기본 이미지
                .error(R.drawable.ic_user) // 에러 시 대체 이미지
                .into(binding.menuImageView)
        }
    }

    // 재료 리스트 ViewHolder
    inner class BibimbabViewHolder(private val binding: IngredientListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IngredientItem) {
            binding.itemTextView.text = item.name
        }
    }
}
