package com.cook_recipe_app.firebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.MenuItem
import com.google.firebase.firestore.FirebaseFirestore

class MenuListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> get() = _menuItems

    fun loadMenuItems() {
        db.collection("menuItems")
            .orderBy("category")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val items = mutableListOf<MenuItem>()
                    var currentCategory = ""
                    for (document in task.result) {
                        val category: String = document.getString("category") ?: ""
                        val name: String = document.getString("name") ?: ""
                        val id: String = document.id

                        // 카테고리별로 구분
                        if (currentCategory != category) {
                            items.add(MenuItem(MenuItem.TYPE_HEADER, category))
                            currentCategory = category
                        }

                        items.add(MenuItem(MenuItem.TYPE_ITEM, name, id))
                    }
                    _menuItems.value = items
                } else {
                    // 오류 발생 시 빈 리스트 전달
                    _menuItems.value = emptyList()
                }
            }
    }
}
