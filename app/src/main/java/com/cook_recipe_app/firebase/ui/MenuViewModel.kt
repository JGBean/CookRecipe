package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.Menu
import com.google.firebase.firestore.FirebaseFirestore

class MenuViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _allMenuItems = MutableLiveData<List<Menu>>()

    private val _filteredMenuItems = MutableLiveData<List<Menu>>()
    val filteredMenuItems: LiveData<List<Menu>> = _filteredMenuItems

    init {
        fetchMenuItems()
    }

    private fun fetchMenuItems() {
        firestore.collection("menuItems").addSnapshotListener { querySnapshot, _ ->
            val menuItems = querySnapshot?.documents?.mapNotNull { document ->
                document.toObject(Menu::class.java)?.apply {
                    id = document.id
                }
            } ?: emptyList()

            _allMenuItems.value = menuItems
            _filteredMenuItems.value = menuItems
        }
    }

    fun filterMenuItems(query: String) {
        val currentAllItems = _allMenuItems.value ?: emptyList()

        val filteredList = if (query.isEmpty()) {
            currentAllItems
        } else {
            currentAllItems.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _filteredMenuItems.value = filteredList
    }
}