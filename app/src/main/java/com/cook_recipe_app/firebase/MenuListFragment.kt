package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.cook_recipe_app.firebase.databinding.FragmentMenuListBinding
import com.google.firebase.firestore.FirebaseFirestore

class MenuListFragment : Fragment() {
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
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        binding.menuRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Firestore 초기화 및 데이터 로드
        db = FirebaseFirestore.getInstance()

        // 어댑터 초기화, 클릭 리스너 추가
        adapter = MenuAdapter(menuItems) { menuItem ->
            if (menuItem.name == "비빔밥") {
                // BibimbabFragment로 이동하기 위한 FragmentTransaction 설정
                val fragment = BibimbabFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment) // fragment_container는 현재 프래그먼트가 표시되는 컨테이너의 ID입니다.
                    .addToBackStack(null) // 뒤로 가기 시 이전 프래그먼트로 돌아가기 위해 추가
                    .commit()
            }
        }
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

                        if (currentCategory != category) {
                            menuItems.add(MenuItem(MenuItem.TYPE_HEADER, category))
                            currentCategory = category
                        }

                        menuItems.add(MenuItem(MenuItem.TYPE_ITEM, name))
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
