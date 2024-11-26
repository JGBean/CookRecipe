package com.cook_recipe_app.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerViewAdapter(
    firestore: FirebaseFirestore,
    private val navigateToMainActivity: (String, String) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val allMenuItems = mutableListOf<Menu>()
    private val filteredMenuItems = mutableListOf<Menu>()

    init {
        firestore.collection("menuItems").addSnapshotListener { querySnapshot, _ ->
            allMenuItems.clear()
            querySnapshot?.documents?.mapNotNull {
                it.toObject(Menu::class.java)?.apply { id = it.id }
            }?.let {
                allMenuItems.addAll(it)
                filteredMenuItems.clear()
                filteredMenuItems.addAll(it)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menuitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = filteredMenuItems[position]
        holder.bind(menuItem)
    }

    fun filter(query: String) {
        filteredMenuItems.clear()
        if (query.isEmpty()) {
            filteredMenuItems.addAll(allMenuItems)
        } else {
            filteredMenuItems.addAll(allMenuItems.filter {
                it.name.contains(query)
            })
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = filteredMenuItems.size

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

