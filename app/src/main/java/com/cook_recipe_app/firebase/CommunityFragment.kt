package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class CommunityFragment : BaseFragment() {
    private val postList = mutableListOf<Post>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rec_posts)  // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)  // 수직 레이아웃 매니저 설정
        postAdapter = PostAdapter(postList, requireContext())  // 어댑터 초기화
        recyclerView.adapter = postAdapter  // RecyclerView에 어댑터 설정

        // Firestore에서 게시물 가져오기
        fetchPostsFromFirestore()

        // FAB 설정
        fab = view.findViewById(R.id.fab_post)
        fab.setOnClickListener {
            val intent = Intent(activity, CommunityPostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPostsFromFirestore() {
        val db = FirebaseFirestore.getInstance()  // Firestore 인스턴스 가져오기
        db.collection("posts")  // 'posts' 컬렉션에서 데이터 가져오기
            .orderBy("timestamp", Query.Direction.DESCENDING) // 최신 게시글부터 정렬
            .get()
            .addOnSuccessListener { documents ->
                postList.clear() // 기존 목록을 초기화
                for (document in documents) {
                    val post = document.toObject(Post::class.java)  // Post 데이터 모델로 변환
                    postList.add(post)  // 게시글 리스트에 추가
                }
                postAdapter.notifyDataSetChanged()  // RecyclerView 갱신
            }
            .addOnFailureListener { exception ->
                // 에러 처리
            }
    }
}