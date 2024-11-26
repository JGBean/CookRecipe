package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentMenuListBinding
import com.cook_recipe_app.firebase.viewmodel.MenuListViewModel

class MenuListFragment : Fragment() {
    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private val viewModel: MenuListViewModel by viewModels()

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
        adapter = MenuAdapter(emptyList()) { menuItem ->
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

        // LiveData 구독
        viewModel.menuItems.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateData(items)
        })

        // 데이터 로드
        viewModel.loadMenuItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
