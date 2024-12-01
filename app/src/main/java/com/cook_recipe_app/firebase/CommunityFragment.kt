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
import androidx.navigation.fragment.findNavController

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private val viewModel: CommunityViewModel by viewModels() // viewModels()에 관리를 위임

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
            val adapter = PostAdapter(posts, requireContext()) { post ->
                navigateToPostDetail(post) // 제목 클릭 시 본문으로 이동
            }
            binding.recPosts.adapter = adapter
        }

        viewModel.fetchPosts() // 게시글 데이터 가져오기

        binding.fabPost.setOnClickListener {
            navigateToPostActivity() // FAB 설정
        }
    }

    private fun navigateToPostDetail(post: Post) {
        // 새로운 PostDetailFragment 생성
        val postDetailFragment = PostDetailFragment()

        // Bundle을 사용해 게시글 데이터를 전달
        val bundle = Bundle().apply {
            putString("title", post.title)
            putString("content", post.content)
        }

        // 데이터를 PostDetailFragment에 전달
        postDetailFragment.arguments = bundle

        // FragmentTransaction을 통해 PostDetailFragment로 전환
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, postDetailFragment) // 현재 프래그먼트 교체
            .addToBackStack(null) // 백 스택에 추가하여 뒤로가기 시 이전 화면으로 돌아갈 수 있도록 함
            .commit()
    }

    private fun navigateToPostActivity() {
        val intent = Intent(requireContext(), CommunityPostActivity::class.java)
        startActivity(intent) // Activity로 전환하는 함수설정
    }
}