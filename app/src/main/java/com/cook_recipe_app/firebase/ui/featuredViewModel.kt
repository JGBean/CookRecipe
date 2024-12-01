package com.cook_recipe_app.firebase.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.featuredItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class featuredViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()


    private val _bookMarkItems = MutableLiveData<List<featuredItem>>()
    val bookMarkItems: LiveData<List<featuredItem>> get() = _bookMarkItems

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId


    // 특정 문서의 데이터를 가져오는 메서드
    fun loadBookmarkedItems() {
        db.collection("likes") // 컬렉션 이름
            .get()
            .addOnSuccessListener { result ->
                // 조건을 만족하는 데이터를 필터링
                val items = result.mapNotNull { document ->
                    val username = document.getString("userId")
                    val menuId = document.getString("menuId")

                    // 조건: userId가 username과 같은 경우만
                    if (username != null && username == userId.value && menuId != null) {
                        featuredItem(menuId)
                    } else {
                        null // 조건에 맞지 않으면 제외
                    }
                }

                // 필터링된 결과를 LiveData에 설정
                _bookMarkItems.value = items
            }
            .addOnFailureListener {
                _bookMarkItems.value = emptyList() // 실패 시 빈 리스트로 설정
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
}