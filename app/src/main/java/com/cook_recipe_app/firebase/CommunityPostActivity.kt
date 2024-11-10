package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton

class CommunityPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_community_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.PostLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 뒤로가기 버튼 설정
        val backButton: ImageButton = findViewById(R.id.imageButton3)
        backButton.setOnClickListener {
            finish() // 액티비티 종료
        }
    }
}