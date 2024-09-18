package com.example.coffeeapp.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        setupRecyclerView()
        setupObservers()
        orderViewModel.fetchOrders()
    }

    private fun setupRecyclerView() {
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(mutableListOf())
        binding.orderRecyclerView.adapter = orderAdapter
    }

    private fun setupObservers() {
        orderViewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders.toMutableList())
        }

        orderViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

