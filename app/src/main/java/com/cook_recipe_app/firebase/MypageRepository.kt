package com.cook_recipe_app.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MypageRepository {
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    fun observeName(name: MutableLiveData<String>) {

        auth = FirebaseAuth.getInstance()
        db.collection("users").document(auth.uid.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("name")
                    if (username != null) {
                        name.value = username
                    } else {
                        Log.w("Mypagefragment", "값이 존재하지 않습니다.")
                    }
                } else {
                    Log.w("MypageRepository", "값이 존재하지 않습니다.")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MypageRepository", "Error getting document: ", exception)
            }
    }
}