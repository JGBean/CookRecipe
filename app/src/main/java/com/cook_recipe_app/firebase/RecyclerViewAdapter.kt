package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuRecyclerViewAdapter(
    private val navigateToMainActivity: (String, String) -> Unit
) : RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder>() {

    private var menuItems: List<Menu> = listOf()

    fun updateItems(newItems: List<Menu>) {
        menuItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menuitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)
    }

    override fun getItemCount() = menuItems.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name)

        fun bind(menuItem: Menu) {
            nameTextView.text = menuItem.name
            itemView.setOnClickListener {
                navigateToMainActivity(menuItem.id, menuItem.name)
            }
        }
    }
}

