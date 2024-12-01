package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cook_recipe_app.firebase.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 번들 데이터 수신
        val title = arguments?.getString("title")
        val content = arguments?.getString("content")

        // 데이터를 UI에 표시
        binding.txtPostTitle.text = title
        binding.txtPostContent.text = content
    }
}