package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cook_recipe_app.firebase.databinding.FragmentRecipeBinding

class RecipeFragment : BaseFragment() {

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
            replaceFragment(CountryRecipeFragment.newInstance())
        }
    }

    // Fragment 교체 함수
    private fun replaceFragment(fragment: Fragment) {
        // `R.id.fragment_container`는 activity_main.xml에서 정의한 FrameLayout입니다.
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment) // activity_main.xml의 컨테이너 ID로 교체
            addToBackStack(null) // 뒤로 가기 버튼으로 돌아갈 수 있게 설정
            commit()
        }
    }
}
