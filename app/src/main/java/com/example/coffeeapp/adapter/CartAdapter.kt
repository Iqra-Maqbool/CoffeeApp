// CartAdapter.kt
package com.example.coffeeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.databinding.CartItemsBinding
import com.example.coffeeapp.models.CartModelClass



class CartAdapter(private val coffeeItems: List<CartModelClass>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coffeeItem: CartModelClass) {
            binding.coffeeName.text = coffeeItem.name
            binding.itemPrice.text = coffeeItem.price.toString()
            binding.quantity.text = coffeeItem.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(coffeeItems[position])
    }

    override fun getItemCount(): Int = coffeeItems.size
}
