package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.databinding.MenuHeaderBinding
import com.cook_recipe_app.firebase.databinding.MenuListBinding

class MenuAdapter(
    private var menuItems: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 데이터 갱신 메서드
    fun updateData(newMenuItems: List<MenuItem>) {
        menuItems = newMenuItems
        notifyDataSetChanged()
    }

    // ViewHolder for Header
    inner class HeaderViewHolder(private val binding: MenuHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            binding.menuHeader.text = header
        }
    }

    // ViewHolder for Item
    inner class ItemViewHolder(private val binding: MenuListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem) {
            binding.menuText.text = menuItem.name
            binding.root.setOnClickListener {
                onItemClick(menuItem) // 아이템 클릭 시 리스너 호출
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return menuItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MenuItem.TYPE_HEADER) {
            val binding = MenuHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = MenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = menuItems[position]
        if (holder is HeaderViewHolder) {
            holder.bind(menuItem.name) // 헤더에는 카테고리 이름이 표시됩니다.
        } else if (holder is ItemViewHolder) {
            holder.bind(menuItem) // 아이템에는 메뉴 이름이 표시됩니다.
        }
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }
}
