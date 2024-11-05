package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cook_recipe_app.firebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val id = binding.etId.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInputs(name, id, email, password, confirmPassword)) {
                registerUser(name, id, email, password)
            }
        }
    }

    private fun validateInputs(name: String, id: String, email: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty() || id.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "비밀번호는 최소 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(name: String, id: String, email: String, password: String) {
        checkDuplicateEmailAndId(email, id) { isDuplicate, duplicateField ->
            if (isDuplicate) {
                Toast.makeText(this, "이미 사용 중인 ${duplicateField}입니다.", Toast.LENGTH_SHORT).show()
            } else {
                createUserWithEmailAndPassword(name, id, email, password)
            }
        }
    }

    private fun checkDuplicateEmailAndId(email: String, id: String, callback: (Boolean, String) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { emailDocuments ->
                if (!emailDocuments.isEmpty) {
                    callback(true, "이메일")
                } else {
                    db.collection("users")
                        .whereEqualTo("id", id)
                        .get()
                        .addOnSuccessListener { idDocuments ->
                            if (!idDocuments.isEmpty) {
                                callback(true, "ID")
                            } else {
                                callback(false, "")
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "ID 중복 확인 실패", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "이메일 중복 확인 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createUserWithEmailAndPassword(name: String, id: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userData = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "id" to id
                        )

                        db.collection("users").document(it.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입 성공! 이메일로 전송된 인증 링크를 클릭하여 계정을 활성화해주세요.", Toast.LENGTH_LONG).show()
                                user.sendEmailVerification()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "데이터 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}