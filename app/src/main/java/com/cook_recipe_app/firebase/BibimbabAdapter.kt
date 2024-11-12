package com.cook_recipe_app.firebase


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.R

class BibimbabAdapter(private val items: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_TEXT = 1
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pic_bibimbab)
    }

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.findViewById(R.id.itemTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_IMAGE else TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_bibimbaab_img, parent, false)
            ImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_list, parent, false)
            TextViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.imageView.setImageResource(R.drawable.img_bibimbab)
        } else if (holder is TextViewHolder) {
            val textPosition = position - 1
            val item = items[textPosition]

            holder.itemTextView.text = item

            // 제목일 경우 굵은 글씨 스타일 적용
            if (item == "재료" || item == "양념장" || item == "요리 방법") {
                holder.itemTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                holder.itemTextView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
