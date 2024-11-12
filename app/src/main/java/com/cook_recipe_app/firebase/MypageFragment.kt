package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.navigation.fragment.findNavController
import com.cook_recipe_app.firebase.R
import com.cook_recipe_app.firebase.databinding.FragmentMypageBinding


class MypageFragment : BaseFragment() {

    var binding: FragmentMypageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ToFeatured?.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, featured())
                .addToBackStack(null)
                .commit()
        }
        //binding?.ToFeatured?.setOnClickListener{
            //findNavController().navigate(R.id.action_mypageFragment_to_featured)
        //}
    }
}