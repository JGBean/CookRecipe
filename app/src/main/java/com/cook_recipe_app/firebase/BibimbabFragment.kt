package com.cook_recipe_app.firebase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cook_recipe_app.firebase.databinding.FragmentBibimbabBinding
import com.google.firebase.firestore.FirebaseFirestore

class BibimbabFragment : Fragment() {
    private var isLiked = false
    private lateinit var binding: FragmentBibimbabBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBibimbabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 좋아요 버튼 설정
        val likeButton: ImageView = binding.likeButton
        likeButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_full)
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_empty)
            }
        }

        // 타이머 이미지 클릭 리스너 설정
        binding.timer.setOnClickListener {
            navigateToTimerFragment()
        }

        // RecyclerView 설정
        binding.bibimbabRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Firebase에서 데이터 가져오기
        db.collection("recipes").document("bibimbab")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Firestore 데이터 가져오기
                    val ingredients = document.get("ingredients") as List<String>
                    val seasoning = document.get("seasoning") as List<String>
                    val cookingSteps = listOf(
                        document.getString("bibimbab_cooking1"),
                        document.getString("bibimbab_cooking2"),
                        document.getString("bibimbab_cooking3"),
                        document.getString("bibimbab_cooking4"),
                        document.getString("bibimbab_cooking5"),
                        document.getString("bibimbab_cooking6"),
                        document.getString("bibimbab_cooking7")
                    ).filterNotNull()

                    // 모든 데이터를 하나의 리스트에 합쳐서 RecyclerView에 표시
                    val allItems = mutableListOf<String>()
                    allItems.add("재료")
                    allItems.addAll(ingredients)
                    allItems.add("양념장")
                    allItems.addAll(seasoning)
                    allItems.add("요리 방법")
                    allItems.addAll(cookingSteps)

                    // Adapter 설정
                    val adapter = BibimbabAdapter(allItems)
                    binding.bibimbabRecyclerView.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                // 오류 처리
                Log.w("BibimbabFragment", "Error getting documents: ", exception)
            }
    }

    private fun navigateToTimerFragment() {
        val timerFragment = TimerFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, timerFragment)
            .addToBackStack(null)
            .commit()
    }
}