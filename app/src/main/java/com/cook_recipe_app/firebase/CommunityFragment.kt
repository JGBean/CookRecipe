package com.cook_recipe_app.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.cook_recipe_app.firebase.R


class CommunityFragment : BaseFragment() {
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 Inflate하여 View 객체를 생성
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        // FAB 설정
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(activity, CommunityPostActivity::class.java)
            startActivity(intent)
        }
        return view  // Inflate된 view를 반환
    }
}