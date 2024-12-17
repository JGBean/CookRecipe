package com.cook_recipe_app.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cook_recipe_app.firebase.databinding.ActivityCommunityPostBinding
import com.cook_recipe_app.firebase.ui.CommunityViewModel
import com.google.firebase.auth.FirebaseAuth

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
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "" // Get the current user's ID
            val post = Post(title = title, content = content, userId = userId)

            viewModel.addPost(post) { success ->
                if (success) {
                    finish() // Close the activity after successful post
                } else {
                    // Show an error message
                    Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.imbBack.setOnClickListener {
            finish() // Back button
        }
    }
}
