package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cook_recipe_app.firebase.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // FragmentRecipeBinding 초기화
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 카드뷰 클릭 리스너 설정
        binding.country.setOnClickListener {
            replaceFragment(CountryRecipeFragment.newInstance()) // 예시 프래그먼트로 변경
        }

//        binding.recipeIngredient.setOnClickListener {
//            replaceFragment(IngredientRecipeFragment.newInstance()) // 예시 프래그먼트로 변경
//        }
//
//        binding.recipeEasy.setOnClickListener {
//            replaceFragment(EasyRecipeFragment.newInstance()) // 예시 프래그먼트로 변경
//        }
    }

    // Fragment 교체 함수
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_recipe1, fragment) // FrameLayout의 id
            addToBackStack(null) // 뒤로 가기 버튼을 눌렀을 때 이전 프래그먼트로 돌아가기
            commit()
        }
    }
}
