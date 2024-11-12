package com.cook_recipe_app.firebase.ui;

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BibimbabViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // 데이터 변경을 감지할 수 있는 LiveData 변수
    private val _bibimbabData = MutableLiveData<List<String>>()
    val bibimbabData: LiveData<List<String>> get() = _bibimbabData

    // Firebase에서 데이터를 가져오는 함수
    fun loadBibimbabData() {
        db.collection("recipes").document("bibimbab")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Firestore에서 데이터를 가져와 리스트로 변환
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

                    // 모든 데이터를 하나의 리스트로 합침
                    val allItems = mutableListOf<String>()
                    allItems.add("재료")
                    allItems.addAll(ingredients)
                    allItems.add("양념장")
                    allItems.addAll(seasoning)
                    allItems.add("요리 방법")
                    allItems.addAll(cookingSteps)

                    // 데이터를 LiveData에 저장
                    _bibimbabData.value = allItems
                }
            }
            .addOnFailureListener { exception ->
                Log.w("BibimbabViewModel", "Error getting documents: ", exception)
            }
    }
}
