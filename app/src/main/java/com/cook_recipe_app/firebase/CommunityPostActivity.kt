package com.cook_recipe_app.firebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import com.google.firebase.firestore.FirebaseFirestore

class CommunityPostActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_community_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.PostLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // EditText 초기화
        etTitle = findViewById(R.id.Edit_Title)
        etContent = findViewById(R.id.Edit_Contents)

        // 작성하기 버튼 설정
        val btnSubmit: Button = findViewById(R.id.b_post)
        btnSubmit.setOnClickListener {
            submitPost()
        }

        // 뒤로가기 버튼 설정
        val backButton: ImageButton = findViewById(R.id.imb_back)
        backButton.setOnClickListener {
            finish() // 액티비티 종료
        }
    }

    private fun submitPost() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val post = Post(title = title, content = content)

            // Firestore에 게시글 추가
            db.collection("posts")
                .add(post)
                .addOnSuccessListener {
                    // 게시글 작성 후 CommunityFragment로 돌아가기
                    finish()
                }
                .addOnFailureListener { e ->
                    // 에러 처리 (예: Log.e("Firestore", "Error adding document", e))
                }
        }
    }
}