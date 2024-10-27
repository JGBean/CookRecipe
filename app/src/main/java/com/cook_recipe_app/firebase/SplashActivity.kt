package com.cook_recipe_app.firebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cook_recipe_app.MainActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cook_recipe_app.firebase.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //스플래시 화면 표시 후 메인 액티비티로 이동 추후 JoinActivity로 변경
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}