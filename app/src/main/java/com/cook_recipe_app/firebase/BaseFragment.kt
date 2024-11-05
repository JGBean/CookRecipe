package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseFragment : Fragment() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Base layout에 BottomNavigationView를 포함한 레이아웃을 inflate 합니다.
        val view = inflater.inflate(R.layout.activity_main, container, false)
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_view)
        setupBottomNavigation()
        return view
    }

    private fun setupBottomNavigation() {
        // BottomNavigationView 아이템 클릭 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recipe -> navigateTo(RecipeFragment())
                R.id.nav_community -> navigateTo(CommunityFragment())
                R.id.nav_user -> navigateTo(MypageFragment())
            }
            true
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

