package com.cook_recipe_app.firebase.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.featuredItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class featuredViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _bookMarkItems = MutableLiveData<List<featuredItem>>()
    val bookMarkItems: LiveData<List<featuredItem>> get() = _bookMarkItems

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    fun loadBookmarkedItems() {
        db.collection("likes")
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<featuredItem>()

                for (document in result) {
                    val username = document.getString("userId")
                    val menuId = document.getString("menuId")

                    if (username != null && username == userId.value && menuId != null) {
                        fetchMenuName(menuId) { menuName ->
                            if (menuName != null) {
                                items.add(featuredItem(menuId, menuName))
                                _bookMarkItems.value = items
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                _bookMarkItems.value = emptyList()
            }
    }

    private fun fetchMenuName(menuId: String, callback: (String?) -> Unit) {
        db.collection("menuItems")
            .document(menuId)
            .get()
            .addOnSuccessListener { document ->
                val menuName = document.getString("name")
                callback(menuName)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun fetchUserId() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _userId.value = document.getString("id") ?: ""
                    } else {
                        Log.e("featuredViewModel", "User document not found")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("featuredViewModel", "Error fetching user ID", exception)
                }
        }
    }
}//misterraba