package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cook_recipe_app.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleNavigationIntent(intent)

        setupImageViewClickListener()
        setupBottomNavigationView()

    }

    private fun setupImageViewClickListener() {
        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_recipe -> RecipeFragment()
                R.id.nav_community -> CommunityFragment()
                R.id.nav_user -> MypageFragment()
                else -> RecipeFragment()
            }
            navigateToFragment(selectedFragment)
            true
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNavigationIntent(intent)
    }

    private fun handleNavigationIntent(intent: Intent?) {
        val menuId = intent?.getStringExtra("menuId")
        val menuName = intent?.getStringExtra("menuName")

        if (menuId != null && menuName != null) {
            val fragment = BibimbabFragment().apply {
                arguments = Bundle().apply {
                    putString("menuId", menuId)
                    putString("menuName", menuName)
                }
            }
            navigateToFragment(fragment)
        } else if (supportFragmentManager.fragments.isEmpty()) {
            navigateToFragment(RecipeFragment())
        }
    }

    fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}