package com.cook_recipe_app.firebase.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cook_recipe_app.firebase.MenuItem
import com.google.firebase.firestore.FirebaseFirestore

class MenuListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: MenuAdapter? = null
    private var menuItems: MutableList<MenuItem>? = null
    private var db: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment에서 사용하는 XML 레이아웃 파일을 inflate합니다. 예를 들어 fragment_menu_list.xml
        return inflater.inflate(R.layout.fragment_menu_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(context))

        menuItems = ArrayList<MenuItem>()
        adapter = MenuAdapter(menuItems)
        recyclerView.setAdapter(adapter)

        // Firestore 초기화 및 데이터 로드
        db = FirebaseFirestore.getInstance()
        loadMenuItems()
    }

    private fun loadMenuItems() {
        db.collection("menuItems")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    var currentCategory = ""
                    for (document in task.getResult()) {
                        val category: String = document.getString("category")
                        val name: String = document.getString("name")

                        // 새로운 카테고리가 나오면 헤더 추가
                        if (currentCategory != category) {
                            menuItems!!.add(MenuItem(MenuItem.TYPE_HEADER, category))
                            currentCategory = category
                        }

                        // 메뉴 아이템 추가
                        menuItems!!.add(MenuItem(MenuItem.TYPE_ITEM, name))
                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException())
                }
            }
    }
}
