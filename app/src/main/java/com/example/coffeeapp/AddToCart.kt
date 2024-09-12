
package com.example.coffeeapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.adapter.CartAdapter
import com.example.coffeeapp.databinding.ActivityAddToCart2Binding
import com.example.coffeeapp.models.CartModelClass


class AddToCart : AppCompatActivity() {
    private lateinit var binding: ActivityAddToCart2Binding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToCart2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("EXTRA_NAME") ?: ""
        val description = intent.getStringExtra("EXTRA_DESCRIPTION") ?: ""
        val price = intent.getDoubleExtra("EXTRA_PRICE", 0.0)
        val quantity = intent.getIntExtra("EXTRA_QUANTITY", 1)

        val coffeeItem = CartModelClass(name, description, price, quantity)
        setupRecyclerView(listOf(coffeeItem))
    }

    private fun setupRecyclerView(coffeeItems: List<CartModelClass>) {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(coffeeItems)
        binding.cartRecyclerView.adapter = cartAdapter
    }
}



