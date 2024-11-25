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

        // 텍스트 스타일 설정
        holder.bind(item) // 기본 바인딩 작업
        if (item == "재료" || item == "양념장" || item == "요리 방법") {
            holder.setBoldText() // 굵은 텍스트 설정
        } else {
            holder.setNormalText() // 일반 텍스트 설정
        }
    }

    // 뷰 홀더 클래스 수정
    inner class BibimbabViewHolder(private val binding: IngredientListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.itemTextView.text = item
        }

        // 텍스트를 굵게 설정하는 메서드
        fun setBoldText() {
            binding.itemTextView.setTypeface(null, android.graphics.Typeface.BOLD)
        }

        // 텍스트를 일반 스타일로 설정하는 메서드
        fun setNormalText() {
            binding.itemTextView.setTypeface(null, android.graphics.Typeface.NORMAL)
        }
    }


    override fun getItemCount(): Int = items.size


}


