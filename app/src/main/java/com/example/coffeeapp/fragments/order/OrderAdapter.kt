
package com.example.coffeeapp.fragments.order
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.databinding.OrderItemsBinding


class OrderAdapter(


    private var orders: MutableList<OrderModel>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: OrderItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderModel) {
            binding.coffeeeName.text = order.name
            binding.Quantity.text = order.quantity.toString()
            binding.itemsPrice.text = order.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: MutableList<OrderModel>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
