package com.example.coffeeapp.fragments.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.databinding.CategoryItemsBinding

class CategoryAdapter(private var items: MutableList<CategoryModel>)
    : RecyclerView.Adapter<CategoryAdapter.MyCategoryViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class MyCategoryViewHolder(var binding: CategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(
            CategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        val data = items[holder.adapterPosition]

        holder.binding.apply {
            titleCat.text = data.title

            holder.binding.root.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                lastSelectedPosition = selectedPosition
                selectedPosition = adapterPosition

                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
            }

            if (selectedPosition == holder.adapterPosition) {
                titleCat.setBackgroundColor(Color.parseColor("#C67C4E"))
            } else {
                titleCat.setBackgroundColor(Color.WHITE)
            }
        }
    }

    fun updateCategoryList(newItems: MutableList<CategoryModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}

