package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cook_recipe_app.firebase.databinding.ActivityCommunityPostBinding
import com.cook_recipe_app.firebase.ui.CommunityViewModel

class CommunityPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityPostBinding
    private val viewModel: CommunityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bPost.setOnClickListener {
            val title = binding.EditTitle.text.toString()
            val content = binding.EditContents.text.toString()
            val post = Post(title = title, content = content)

            viewModel.addPost(post) { success ->
                if (success) {
                    finish() // 게시글 작성 후 종료
                }
            }
        }
    }
}