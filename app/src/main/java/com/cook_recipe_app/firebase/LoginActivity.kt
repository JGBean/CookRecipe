package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cook_recipe_app.firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val id = binding.idEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (id.isNotEmpty() && password.isNotEmpty()) {
                loginUser(id, password)
            } else {
                Toast.makeText(this, "ID와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpButton.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }

    private fun loginUser(id: String, password: String) {
        db.collection("users")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "존재하지 않는 ID입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val userDoc = documents.documents[0]
                    val email = userDoc.getString("email")
                    if (email != null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user != null) {
                                        if (user.isEmailVerified) {
                                            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                                            val mainIntent = Intent(this, MainActivity::class.java)
                                            startActivity(mainIntent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show()
                                            auth.signOut() // 인증되지 않은 사용자는 로그아웃 처리
                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "로그인 실패: 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "사용자 정보 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "로그인 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}