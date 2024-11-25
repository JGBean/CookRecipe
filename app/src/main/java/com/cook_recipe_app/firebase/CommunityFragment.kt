package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentCommunityBinding
import com.cook_recipe_app.firebase.ui.CommunityViewModel

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private val viewModel: CommunityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recPosts.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel의 postList를 관찰하여 RecyclerView 업데이트
        viewModel.postList.observe(viewLifecycleOwner) { posts ->
            val adapter = PostAdapter(posts, requireContext())
            binding.recPosts.adapter = adapter
        }

        // 데이터 가져오기
        viewModel.fetchPosts()

        binding.fabPost.setOnClickListener {
            navigateToPostActivity()
        }
    }

    private fun navigateToPostActivity() {
        val intent = Intent(requireContext(), CommunityPostActivity::class.java)
        startActivity(intent) // Activity로 전환
    }
}