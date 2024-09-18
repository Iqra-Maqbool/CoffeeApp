

package com.example.coffeeapp.fragments.addToCart
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.databinding.FragmentFavouriteBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddToCartFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val deliveryCharge = 200.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val user = auth.currentUser
        user?.let {
            cartViewModel.fetchCartItems(it.uid)
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.updateItems(items)
            calculateAndDisplaySubtotalAndTotal(items)  // Update subtotal and total when items change
        }

        // Set up Buy Now button click listener
        binding.buyNowButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                addCartItemsToOrders(currentUser.uid)
            } else {
                Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView() {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(mutableListOf(), cartViewModel,
            { updatedItems ->
                calculateAndDisplaySubtotalAndTotal(updatedItems)  // Update subtotal and total when items are updated
            },
            { itemToDelete ->
                cartViewModel.deleteItem(itemToDelete)
            }
        )
        binding.cartRecyclerView.adapter = cartAdapter
    }

    // Function to calculate subtotal, add delivery charge, and display total
    private fun calculateAndDisplaySubtotalAndTotal(items: MutableList<CartModelClass>) {
        var subtotal = 0.0
        for (item in items) {
            subtotal += item.unitPrice * item.quantity
        }

        val total = subtotal + deliveryCharge
        binding.subTotal.text = subtotal.toString()
        binding.totalAmount.text = total.toString()
    }


    // Function to add cart items to Firestore orders sub-collection
    private fun addCartItemsToOrders(userId: String) {
        val cartItems = cartViewModel.cartItems.value ?: mutableListOf()

        // Reference to the user's "orders" sub-collection
        val ordersRef = firestore.collection("users")
            .document(userId)
            .collection("orders")

        val batch = firestore.batch()  // Batch write to Firestore for all cart items

        for (cartItem in cartItems) {
            val orderData = hashMapOf(
                "name" to cartItem.name,
                "quantity" to cartItem.quantity,
                "price" to cartItem.unitPrice * cartItem.quantity
            )
            val newOrderRef = ordersRef.document()  // Create new document in the orders sub-collection
            batch.set(newOrderRef, orderData)
        }

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show()
                clearCart()  // Clear the cart after placing the order
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to place order: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun clearCart() {
        cartViewModel.clearCart()
    }
}
