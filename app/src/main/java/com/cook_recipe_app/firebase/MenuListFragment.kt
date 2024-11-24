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

class MenuListFragment : Fragment() {
    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private val menuItems: MutableList<MenuItem> = mutableListOf()  // 여기서 menuItems 초기화
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 레이아웃 설정
        binding.menuRecycler.layoutManager = LinearLayoutManager(requireContext())

        // 어댑터 초기화
        adapter = MenuAdapter(menuItems) { menuItem ->
            // 메뉴 항목 클릭 시 처리
            if (menuItem.type == MenuItem.TYPE_ITEM) {
                val bibimbabFragment = BibimbabFragment()
                val bundle = Bundle()
                bundle.putString("menuId", menuItem.id)  // 메뉴 ID를 전달
                bibimbabFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, bibimbabFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        binding.menuRecycler.adapter = adapter

        // Firebase 초기화
        db = FirebaseFirestore.getInstance()
        loadMenuItems()
    }

    private fun loadMenuItems() {
        db.collection("menuItems")
            .orderBy("category")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    menuItems.clear()
                    var currentCategory = ""
                    for (document in task.result) {
                        val category: String = document.getString("category") ?: ""
                        val name: String = document.getString("name") ?: ""
                        val id: String = document.id  // 메뉴 아이디

                        // 카테고리별로 구분
                        if (currentCategory != category) {
                            menuItems.add(MenuItem(MenuItem.TYPE_HEADER, category))
                            currentCategory = category
                        }

                        menuItems.add(MenuItem(MenuItem.TYPE_ITEM, name, id))  // 메뉴 이름과 ID 추가
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("MenuListFragment", "Error loading menu items", task.exception)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
