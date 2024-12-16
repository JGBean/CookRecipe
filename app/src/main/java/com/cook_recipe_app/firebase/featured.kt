package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentFeaturedBinding
import com.cook_recipe_app.firebase.ui.featuredViewModel

class featured : BaseFragment() {
    private val viewModel: featuredViewModel by activityViewModels()
    private lateinit var binding: FragmentFeaturedBinding
    private lateinit var adapter: featuredAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        adapter = featuredAdapter(emptyList()) { menuId ->
            navigateToBibimbabFragment(menuId) // 메뉴 ID를 전달하며 이동
        }
        binding.recFeatured.layoutManager = LinearLayoutManager(context)
        binding.recFeatured.adapter = adapter

        // ViewModel 데이터 관찰
        viewModel.bookMarkItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
        }
        viewModel.fetchUserId()
        viewModel.loadBookmarkedItems()
    }

    private fun navigateToBibimbabFragment(menuId: String) {
        val bibimbabFragment = BibimbabFragment().apply {
            arguments = Bundle().apply {
                putString("menuId", menuId) // 메뉴 ID 전달
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, bibimbabFragment)
            .addToBackStack(null)
            .commit()
    }
}