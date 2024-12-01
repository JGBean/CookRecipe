package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.cook_recipe_app.firebase.Post
import com.google.firebase.firestore.Query

class CommunityViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _postList = MutableLiveData<List<Post>>() // ViewModel 내부에서 내용을 수정하거나 갱신할 때의 변수
    val postList: LiveData<List<Post>> get() = _postList // _postList의 내용을 받아서 외부에 표시하는 변수

    fun fetchPosts() {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING) // 최신 순으로 정렬
            .get()
            .addOnSuccessListener { documents ->
                val posts = documents.map { document ->
                    document.toObject(Post::class.java).copy(id = document.id) // ID 추가
                }
                _postList.value = posts
            }
            .addOnFailureListener {
                _postList.value = emptyList() // 오류 발생 시 빈 리스트 반환
            }
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}