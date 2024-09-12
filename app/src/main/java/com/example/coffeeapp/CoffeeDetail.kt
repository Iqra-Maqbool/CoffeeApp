
package com.example.coffeeapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.coffeeapp.databinding.ActivityCoffeeDetailBinding

class CoffeeDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCoffeeDetailBinding
    private var quantity = 1
    private var price = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoffeeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val name = intent.getStringExtra("EXTRA_NAME")
        val description = intent.getStringExtra("EXTRA_DESCRIPTION")
        price = intent.getDoubleExtra("EXTRA_PRICE", 0.0)
       val image = intent.getStringExtra("EXTRA_IMAGE")




        binding.showCname.text = name
        binding.showCdescription.text = description
        updatePriceTextView()
        Glide.with(this)
            .load(image)
            .into(binding.showCoffeeImg)





        binding.addToCart.setOnClickListener {
            val intent = Intent(this, AddToCart::class.java).apply {
                putExtra("EXTRA_NAME", name)
                putExtra("EXTRA_DESCRIPTION", description)
                putExtra("EXTRA_PRICE", price)
                putExtra("EXTRA_QUANTITY", quantity)
            }
            startActivity(intent)
        }


        binding.textView8.setOnClickListener {
            incrementQuantity()
        }

        binding.textView9.setOnClickListener {
            decrementQuantity()
        }
    }

    private fun updateQuantityTextView() {
        binding.quantity.text = quantity.toString()
    }

    private fun updatePriceTextView() {
        val totalPrice = price * quantity
        binding.showCprice.text = String.format("%.2f", totalPrice)
    }

    private fun incrementQuantity() {
        quantity++
        updateQuantityTextView()
        updatePriceTextView()
    }

    private fun decrementQuantity() {
        if (quantity > 1) {
            quantity--
            updateQuantityTextView()
            updatePriceTextView()
        }
    }
}






