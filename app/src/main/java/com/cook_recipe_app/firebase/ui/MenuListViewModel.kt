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

    // 좋아요 수 LiveData 추가
    private val _likesCount = MutableLiveData<Map<String, Int>>()
    val likesCount: LiveData<Map<String, Int>> get() = _likesCount

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
                    // 메뉴 아이템 불러오면서 좋아요 수 집계
                    loadLikesCount()
                } else {
                    // 오류 발생 시 빈 리스트 전달
                    _menuItems.value = emptyList()
                }
            }


    }

    private fun loadLikesCount() {
        db.collection("likes")
            .get()
            .addOnSuccessListener { result ->
                val counts = mutableMapOf<String, Int>()
                for (document in result) {
                    val menuId = document.getString("menuId") ?: ""
                    counts[menuId] = counts.getOrDefault(menuId, 0) + 1
                }
                _likesCount.value = counts
            }
    }
}
