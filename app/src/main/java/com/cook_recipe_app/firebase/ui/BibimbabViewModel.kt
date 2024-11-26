package com.cook_recipe_app.firebase.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BibimbabViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _menuTitle = MutableLiveData<String>()
    val menuTitle: LiveData<String> get() = _menuTitle
    //LiveData : 관찰 가능한 데이터 홀더 클래스 (읽기 전용)
    private val _menuItems = MutableLiveData<List<String>>()
    val menuItems: LiveData<List<String>> get() = _menuItems
    //MutableLiveData : LiveData를 확장. 데이터 변경 가능함.
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    fun fetchMenuData(menuId: String) {
        db.collection("recipes").document(menuId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _menuTitle.value = document.getString("name") ?: "Unknown"

                    val ingredients = document.get("ingredients") as? List<String> ?: emptyList()
                    val seasoning = document.get("seasoning") as? List<String> ?: emptyList()
                    val steps = document.get("steps") as? List<String> ?: emptyList()

                    val allItems = mutableListOf<String>().apply {
                        add("재료")
                        addAll(ingredients)
                        add("양념장")
                        addAll(seasoning)
                        add("요리 방법")
                        addAll(steps)
                    }

                    _menuItems.value = allItems
                }
            }
    }

    fun fetchUserId() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("user").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    _userId.value = document.getString("id")?: ""
                }
                .addOnFailureListener { exception ->
                    Log.e("BibimbabViewModel", "Error fetching user ID", exception)
                }
        }
    }

    fun toggleLike(menuId: String) {
        val currentUserId = _userId.value ?: return
        val likeDocId = "${currentUserId}_$menuId"

        if (_isLiked.value == true) {
            db.collection("likes").document(likeDocId).delete()
                .addOnSuccessListener { _isLiked.value = false }
                .addOnFailureListener { exception ->
                    Log.e("BibimbabViewModel", "Error unliking", exception)
                }
        } else {
            val likeData = mapOf(
                "userId" to currentUserId,
                "menuId" to menuId,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("likes").document(likeDocId).set(likeData)
                .addOnSuccessListener { _isLiked.value = true }
                .addOnFailureListener { exception ->
                    Log.e("BibimbabViewModel", "Error liking", exception)
                }
        }
    }

    fun checkIsLiked(menuId: String) {
        val currentUserId = _userId.value ?: return
        val likeDocId = "${currentUserId}_$menuId"

        db.collection("likes").document(likeDocId)
            .get()
            .addOnSuccessListener { document ->
                _isLiked.value = document.exists()
            }
    }
}
