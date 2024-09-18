package com.example.coffeeapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeapp.R
import com.example.coffeeapp.databinding.CoffeeItemsBinding

class CoffeeAdapter(
    private var coffeeItems: List<CoffeeModelClass> = emptyList(), // Initialize with an empty list
    private val fragment: HomeFragment
) : RecyclerView.Adapter<CoffeeAdapter.MyCoffeeViewHolder>() {

    class MyCoffeeViewHolder(val binding: CoffeeItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCoffeeViewHolder {
        return MyCoffeeViewHolder(
            CoffeeItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = coffeeItems.size

    override fun onBindViewHolder(holder: MyCoffeeViewHolder, position: Int) {
        val data = coffeeItems[position]

        holder.binding.apply {
            CoffeeName.text = data.name
            description.text = data.description
            showPrice.text = data.price.toString()
            Glide.with(fragment)
                .load(data.image)
                .into(categoryImg)

            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("NAME", data.name)
                    putString("DESCRIPTION", data.description)
                    putDouble("PRICE", data.price)
                    putString("IMAGE", data.image)
                }
                it.findNavController().navigate(R.id.action_homeFragment_to_coffeeDetailFragment, bundle)
            }
        }
    }

    fun updateCoffeeList(newCoffeeItems: MutableList<CoffeeModelClass>) {
        coffeeItems = newCoffeeItems
        notifyDataSetChanged()
    }
}


/*
package com.example.coffeeapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeapp.R
import com.example.coffeeapp.databinding.CoffeeItemsBinding

class CoffeeAdapter(
    private var coffeeItems: MutableList<CoffeeModelClass>,
    private val fragment: com.example.coffeeapp.fragments.home.HomeFragment
) : RecyclerView.Adapter<CoffeeAdapter.MyCategoryViewHolder>() {

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
            Glide.with(fragment)
                .load(data.image)
                .into(categoryImg)

            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("NAME", data.name)
                    putString("DESCRIPTION", data.description)
                    putDouble("PRICE", data.price)
                    putString("IMAGE", data.image)
                }
                it.findNavController().navigate(R.id.action_homeFragment_to_coffeeDetailFragment, bundle)
            }
        }
    }

    fun updateCoffeeList(newCoffeeItems: MutableList<CoffeeModelClass>) {
        coffeeItems = newCoffeeItems
        notifyDataSetChanged()
    }
}
*/

