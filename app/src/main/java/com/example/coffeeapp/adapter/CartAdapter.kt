package com.example.coffeeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.AddToCart
import com.example.coffeeapp.databinding.CartItemsBinding
import com.example.coffeeapp.databinding.CoffeeItemsBinding
import com.example.coffeeapp.models.CartModelClass


class CartAdapter(
    private var cartList: ArrayList<CartModelClass>,
    addToCart: AddToCart,

    ) : RecyclerView.Adapter<CartAdapter.MyCategoryViewHolder>() {
    class MyCategoryViewHolder(var binding: CartItemsBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(
            CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = cartList.size


    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        val data = cartList[position]

        holder.binding.apply {
            coffeeImg.setImageResource(data.itemImage)
            coffeeName.text = data.itemName
            itemPrice.text = data.itemPrice
            quantity.text = data.itemQuantity.toString()
        }
    }

    /* override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
         val data = cartList[position]

         holder.binding.apply{
             categoryImg.setImageResource(data.categoryImage)
            CoffeeType.text = data.categoryText

             }
         }*/
    }




