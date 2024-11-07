package com.cook_recipe_app.firebase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentMenuListBinding
import com.google.firebase.firestore.FirebaseFirestore

class MenuListFragment : BaseFragment() {
    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private val menuItems: MutableList<MenuItem> = mutableListOf()
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding 초기화
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        binding.menuRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Firestore 초기화 및 데이터 로드
        db = FirebaseFirestore.getInstance()

        // Adapter 초기화
        adapter = MenuAdapter(menuItems)
        binding.menuRecycler.adapter = adapter

        loadMenuItems()
    }

    private fun loadMenuItems() {
        db.collection("menuItems")
            .orderBy("category")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var currentCategory = ""
                    for (document in task.result) {
                        val category: String = document.getString("category") ?: ""
                        val name: String = document.getString("name") ?: ""

                        // 새로운 카테고리가 나오면 헤더 추가
                        if (currentCategory != category) {
                            menuItems.add(MenuItem(MenuItem.TYPE_HEADER, category))
                            currentCategory = category
                        }

                        // 메뉴 아이템 추가
                        menuItems.add(MenuItem(MenuItem.TYPE_ITEM, name))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.w("Firestore", "Error getting documents.", task.exception)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}