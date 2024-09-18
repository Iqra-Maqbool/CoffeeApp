package com.example.coffeeapp.fragments.cart
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.databinding.CartItemsBinding

class CartAdapter(
    private var coffeeItems: MutableList<CartModel>,
    private val viewModel: CartViewModel,
    private val onCartUpdated: (MutableList<CartModel>) -> Unit,


    private val onItemDeleted: (CartModel) -> Unit


) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coffeeItem: CartModel) {
            binding.coffeeName.text = coffeeItem.name
            binding.itemPrice.text = String.format("%.2f", coffeeItem.unitPrice * coffeeItem.quantity)
            binding.quantity.text = coffeeItem.quantity.toString()

            binding.increment.setOnClickListener {
                coffeeItem.quantity++
                updateItem(coffeeItem)
            }

            binding.decrement.setOnClickListener {
                if (coffeeItem.quantity > 1) {
                    coffeeItem.quantity--
                    updateItem(coffeeItem)
                }
            }

            binding.delete.setOnClickListener {
                onItemDeleted(coffeeItem)
            }
        }



        private fun updateItem(coffeeItem: CartModel) {

            binding.itemPrice.text = String.format("%.2f", coffeeItem.unitPrice * coffeeItem.quantity)
            binding.quantity.text = coffeeItem.quantity.toString()

            viewModel.updateItem(coffeeItem)
            onCartUpdated(coffeeItems)
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

    fun updateItems(newItems: MutableList<CartModel>) {
        coffeeItems = newItems
        notifyDataSetChanged()
    }
}
