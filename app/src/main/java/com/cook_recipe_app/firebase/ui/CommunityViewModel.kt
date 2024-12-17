package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.cook_recipe_app.firebase.Post
import com.google.firebase.firestore.Query

class CommunityViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _postList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> get() = _postList

    fun fetchPosts() {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val posts = documents.map { document ->
                    document.toObject(Post::class.java).copy(
                        id = document.id,
                        userId = document.getString("userId") ?: ""
                    )
                }
                _postList.value = posts
            }
            .addOnFailureListener {
                _postList.value = emptyList()
            }
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}
