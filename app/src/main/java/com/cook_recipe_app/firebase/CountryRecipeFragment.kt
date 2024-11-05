package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cook_recipe_app.firebase.databinding.FragmentCountryRecipeBinding

class CountryRecipeFragment : BaseFragment() {

    private var _binding: FragmentCountryRecipeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): CountryRecipeFragment {
            return CountryRecipeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding 초기화
        _binding = FragmentCountryRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // korean CardView 클릭 시 MenuListFragment로 이동
        binding.korean.setOnClickListener {
            // Fragment 전환을 위한 FragmentTransaction 사용
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuListFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}
