package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.cook_recipe_app.firebase.databinding.FragmentBibimbabBinding

class BibimbabFragment : Fragment() {
    private var isLiked = false
    private lateinit var binding: FragmentBibimbabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding layout for this fragment
        binding = FragmentBibimbabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likeButton: ImageView = binding.likeButton

        likeButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_full)
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_empty)
            }
        }
    }
}
