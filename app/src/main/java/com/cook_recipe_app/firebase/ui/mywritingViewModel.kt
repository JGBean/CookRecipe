package com.cook_recipe_app.firebase.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.featuredItem
import com.cook_recipe_app.firebase.mywritingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class mywritingViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val _mywritingItems = MutableLiveData<List<mywritingItem>>()
    val mywritingItems: LiveData<List<mywritingItem>> get() = _mywritingItems

    // 특정 문서의 데이터를 가져오는 메서드
    fun loadBookmarkedItems() {
        db.collection("posts") // 컬렉션 이름
            .get()
            .addOnSuccessListener { result ->
                // 조건을 만족하는 데이터를 필터링
                val items = result.mapNotNull { document ->
                    val username = document.getString("userId")
                    val title = document.getString("title")
                    val content = document.getString("content")
                    // 조건: userId가 username과 같은 경우만
                    if (username != null && username == FirebaseAuth.getInstance().currentUser?.uid && title != null) {
                        mywritingItem(title, content ?: "")
                    } else {
                        null // 조건에 맞지 않으면 제외
                    }
                }

                // 필터링된 결과를 LiveData에 설정
                _mywritingItems.value = items
            }
            .addOnFailureListener {
                _mywritingItems.value = emptyList() // 실패 시 빈 리스트로 설정
            }
    }
}
