package com.example.coffeeapp.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.databinding.FragmentOrderBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderAdapter
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val user = auth.currentUser
        user?.let {
            fetchOrders(it.uid)
        }
    }

    private fun setupRecyclerView() {
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(mutableListOf())
        binding.orderRecyclerView.adapter = orderAdapter
    }

    private fun fetchOrders(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("orders")
            .get()
            .addOnSuccessListener { documents ->
                val orderList = mutableListOf<OrderModelClass>()
                for (document in documents) {
                    val order = document.toObject(OrderModelClass::class.java)
                    orderList.add(order)
                }
                orderAdapter.updateOrders(orderList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error fetching orders: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
