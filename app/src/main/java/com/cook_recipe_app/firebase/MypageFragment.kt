package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cook_recipe_app.firebase.R
import com.cook_recipe_app.firebase.databinding.FragmentMypageBinding
import com.cook_recipe_app.firebase.ui.MypageVIewModel

class MypageFragment : BaseFragment() {

    val viewModel: MypageVIewModel by activityViewModels()
    lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.name.observe(viewLifecycleOwner){ username->
            binding.UserNameText.text = username
        }

        binding.ToFeatured.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, featured())
                .addToBackStack(null)
                .commit()
        }
        binding.ToMyWritings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mywriting())
                .addToBackStack(null)
                .commit()
        }
    }
}