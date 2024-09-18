
package com.example.coffeeapp.fragments.coffeeDetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.coffeeapp.R
import com.example.coffeeapp.databinding.FragmentCoffeeDetailBinding
import com.example.coffeeapp.fragments.addToCart.CartModelClass
import com.example.coffeeapp.fragments.addToCart.CartViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CoffeeDetailFragment : Fragment() {
    private lateinit var binding: FragmentCoffeeDetailBinding
    private var quantity = 1
    private var price = 0.0
    private val cartViewModel: CartViewModel by activityViewModels()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoffeeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("NAME") ?: ""
        val description = arguments?.getString("DESCRIPTION") ?: ""
        price = arguments?.getDouble("PRICE", 0.0) ?: 0.0
        val imageUrl = arguments?.getString("IMAGE")

        binding.showCname.text = name
        binding.showCdescription.text = description
        updatePriceTextView()

        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.showCoffeeImg)

        binding.addToCart.setOnClickListener {
            val coffeeItem = CartModelClass(name, description, price, quantity)
            cartViewModel.addItem(coffeeItem)
            it.findNavController().navigate(R.id.action_coffeeDetailFragment_to_addToCartFragment)
        }

        binding.buyNow.setOnClickListener {
            addCoffeeToOrders(name, price, quantity)
        }

        binding.textView8.setOnClickListener { incrementQuantity() }
        binding.textView9.setOnClickListener { decrementQuantity() }
    }

    private fun updatePriceTextView() {
        val totalPrice = price * quantity
        binding.showCprice.text = String.format("%.2f", totalPrice)
    }

    private fun incrementQuantity() {
        quantity++
        binding.quantity.text = quantity.toString()
        updatePriceTextView()
    }

    private fun decrementQuantity() {
        if (quantity > 1) {
            quantity--
            binding.quantity.text = quantity.toString()
            updatePriceTextView()
        }
    }

    private fun addCoffeeToOrders(coffeeName: String, coffeePrice: Double, quantity: Int) {
        val userId = auth.currentUser?.uid ?: return
        val ordersRef = firestore.collection("users").document(userId).collection("orders")

        // Create new document in the orders sub-collection
        val orderData = hashMapOf(
            "name" to coffeeName,
            "quantity" to quantity,
            "price" to coffeePrice * quantity
        )
        val newOrderRef = ordersRef.document()
        newOrderRef.set(orderData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to place order: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up resources
    }
}
