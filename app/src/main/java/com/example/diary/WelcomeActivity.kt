package com.example.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diary.databinding.ActivityMainBinding
import com.example.diary.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.inflateMenu(R.menu.menu)

        setSupportActionBar(binding.toolbar)
        
        binding.Enterbutton.setOnClickListener {
            Log.d("WelcomeActivity", "Enter button clicked")
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 避免返回歡迎頁
        }
    }
}