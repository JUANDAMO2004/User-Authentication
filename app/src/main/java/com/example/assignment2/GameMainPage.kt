package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment2.databinding.ActivityGameMainPageBinding

class GameMainPage : AppCompatActivity() {
    private lateinit var binding: ActivityGameMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnEasy.setOnClickListener{
            val intent = Intent(this, Easy::class.java)
            startActivity(intent)
        }

        binding.btnMedium.setOnClickListener{
            val intent = Intent(this, Medium::class.java)
            startActivity(intent)
        }

        binding.btnAdvanced.setOnClickListener{
            val intent = Intent(this, Advanced::class.java)
            startActivity(intent)
        }

        binding.btnIntermediate.setOnClickListener{
            val intent = Intent(this, Intermediate::class.java)
            startActivity(intent)
        }

        // Go to the main page using back button:
        binding.btnBackToMain.setOnClickListener{
            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}