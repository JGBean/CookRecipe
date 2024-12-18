package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentMywritingBinding
import com.cook_recipe_app.firebase.ui.mywritingViewModel

class mywriting: BaseFragment() {

    private val viewModel: mywritingViewModel by activityViewModels()
    private lateinit var binding: FragmentMywritingBinding
    private lateinit var adapter: mywritingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMywritingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        adapter = mywritingAdapter(emptyList()) { post ->
            navigateToPostFragment(post) // mywritingItem 객체를 전달
        }

        binding.recmywriting.layoutManager = LinearLayoutManager(context)
        binding.recmywriting.adapter = adapter

        // ViewModel 데이터 관찰
        viewModel.mywritingItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
        }
        viewModel.loadBookmarkedItems()
    }

    private fun navigateToPostFragment(post: mywritingItem) {
        // PostDetailFragment로 바로 이동
        val postDetailFragment = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putString("title", post.title)     // title 전달
                putString("content", post.content) // content 전달
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, postDetailFragment) // 프래그먼트 전환
            .addToBackStack(null) // 뒤로가기 기능 추가
            .commit()
    }
}