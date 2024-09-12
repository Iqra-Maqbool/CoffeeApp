// CategoryAdapter.kt
package com.example.coffeeapp.adapter

import CategoryModelClass
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeapp.CoffeeDetail
import com.example.coffeeapp.databinding.CoffeeItemsBinding
import com.example.coffeeapp.fragments.home.HomeFragment


class CategoryAdapter(
    private val coffeeItems: MutableList<CategoryModelClass>,
    private val context: HomeFragment
) : RecyclerView.Adapter<CategoryAdapter.MyCategoryViewHolder>() {

    class MyCategoryViewHolder(val binding: CoffeeItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(
            CoffeeItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = coffeeItems.size

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        val data = coffeeItems[position]

        holder.binding.apply {
            CoffeeName.text = data.name
            description.text = data.description
            showPrice.text = data.price.toString()


            Glide.with(context)
                .load(data.image)
                .into(categoryImg)

            root.setOnClickListener {
                val intent = Intent(context.requireContext(), CoffeeDetail::class.java).apply {
                    putExtra("EXTRA_NAME", data.name)
                    putExtra("EXTRA_DESCRIPTION", data.description)
                    putExtra("EXTRA_PRICE", data.price)
                    putExtra("EXTRA_IMAGE", data.image)
                }
                context.startActivity(intent)
            }
        }
    }


}
