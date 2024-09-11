package com.example.coffeeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.adapter.CartAdapter
import com.example.coffeeapp.databinding.ActivityAddToCartBinding
import com.example.coffeeapp.models.CartModelClass

class AddToCart : AppCompatActivity() {
    private lateinit var binding: ActivityAddToCartBinding

    private lateinit var cartList: ArrayList<CartModelClass>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        cartList = ArrayList<CartModelClass>().apply {
            add(CartModelClass(R.drawable.mocha, "Caffe Mocha", "$5.00", 2))
            add(CartModelClass(R.drawable.white, "Flat White", "$4.50", 1))
            add(CartModelClass(R.drawable.mocha, "Caffe Mocha", "$5.00", 2))
            add(CartModelClass(R.drawable.white, "Flat White", "$4.50", 1))
            add(CartModelClass(R.drawable.mocha, "Caffe Mocha", "$5.00", 2))
            add(CartModelClass(R.drawable.white, "Flat White", "$4.50", 1))
            add(CartModelClass(R.drawable.mocha, "Caffe Mocha", "$5.00", 2))
            add(CartModelClass(R.drawable.white, "Flat White", "$4.50", 1))
        }

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AddToCart, LinearLayoutManager.VERTICAL, false)
            adapter = CartAdapter(cartList, this@AddToCart)
            setHasFixedSize(true)
        }

    }
}