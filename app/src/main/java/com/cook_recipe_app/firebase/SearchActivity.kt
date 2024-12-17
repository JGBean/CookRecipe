package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.ActivitySearchBinding
import com.cook_recipe_app.firebase.ui.MenuViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: MenuRecyclerViewAdapter
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = MenuRecyclerViewAdapter { menuId, menuName ->
            navigateToMainActivity(menuId, menuName)
        }
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.filterMenuItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.filterMenuItems(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.filteredMenuItems.observe(this) { menuItems ->
            adapter.updateItems(menuItems)
        }
    }

    private fun navigateToMainActivity(menuId: String, menuName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("menuId", menuId)
            putExtra("menuName", menuName)
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        }
        startActivity(intent)
    }
}