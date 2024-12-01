package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
        adapter = featuredAdapter(emptyList())
        binding.recFeatured.layoutManager = LinearLayoutManager(context)
        binding.recFeatured.adapter = adapter

        // ViewModel 데이터 관찰
        viewModel.bookMarkItems.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateData(items)
        })
        viewModel.fetchUserId()
        // 특정 문서의 데이터를 가져오기
        viewModel.loadBookmarkedItems()
    }
}//misterraba