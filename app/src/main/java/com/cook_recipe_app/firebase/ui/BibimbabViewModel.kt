package com.cook_recipe_app.firebase.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

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
            // 현재 사용자의 UID를 사용하여 'users' 컬렉션에서 해당 문서를 찾음
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _userId.value = document.getString("id") ?: ""
                    } else {
                        Log.e("BibimbabViewModel", "사용자 문서를 찾을 수 없음")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("BibimbabViewModel", "사용자 ID를 가져오는 중 오류 발생", exception)
                }
        }
    }

    fun fetchImageUrl(menuId: String) {
        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/cook-recipe-app.firebasestorage.app/o/image%2F$menuId.png?alt=media&token=f78a262c-46ee-4528-be20-928883445b5d"

        _imageUrl.value = imageUrl
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