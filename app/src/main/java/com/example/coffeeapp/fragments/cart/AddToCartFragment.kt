package com.example.coffeeapp.fragments.cart
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
class AddToCartFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private val auth = FirebaseAuth.getInstance()

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
        }

        cartViewModel.subTotal.observe(viewLifecycleOwner) { subtotal ->
            binding.subTotal.text = subtotal.toString()
        }

        cartViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.totalAmount.text = total.toString()
        }

        binding.buyNowButton.setOnClickListener {
            val currentUser = auth.currentUser
            currentUser?.let {
                cartViewModel.addCartItemsToOrders(it.uid)
            } ?: run {
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
                cartViewModel.calculateSubtotalAndTotal(updatedItems)
            },
            { itemToDelete ->
                cartViewModel.deleteItem(itemToDelete)
            }
        )
        binding.cartRecyclerView.adapter = cartAdapter
    }
}
