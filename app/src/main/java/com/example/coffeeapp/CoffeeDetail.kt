package com.example.coffeeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeapp.databinding.ActivityCoffeeDetailBinding
import com.example.coffeeapp.databinding.ActivityMainBinding

class CoffeeDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCoffeeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoffeeDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.addToCart.setOnClickListener(){
            var intent = Intent(this, AddToCart::class.java)
            startActivity(intent)
        }
    }
}


