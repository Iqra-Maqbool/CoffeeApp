package com.example.coffeeapp.fragments.home
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.databinding.FragmentHomeBinding
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coffeeViewModel: CoffeeViewModel
    private lateinit var coffeeAdapter: CoffeeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        coffeeViewModel = ViewModelProvider(this)[CoffeeViewModel::class.java]
        setupCategoryRecyclerView()
        setupCoffeeRecyclerView()
        setupSearchView()

        return binding.root
    }


    private fun setupCategoryRecyclerView() {
        binding.CatRV.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        categoryAdapter = CategoryAdapter(mutableListOf())
        binding.CatRV.adapter = categoryAdapter
        categoryViewModel.category.observe(viewLifecycleOwner, Observer { categoryList ->
            if (categoryList != null && categoryList.isNotEmpty()) {
                categoryAdapter.updateCategoryList(categoryList.toMutableList())
            }
        })
        categoryViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        categoryViewModel.loadCategory()
    }



    private fun setupCoffeeRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }

        coffeeViewModel.coffeeList.observe(viewLifecycleOwner, Observer { coffeeList ->
            if (coffeeList != null) {
                coffeeAdapter = CoffeeAdapter(coffeeList, this@HomeFragment)
                binding.categoryRecyclerView.adapter = coffeeAdapter
            }
        })

        coffeeViewModel.filteredCoffeeList.observe(viewLifecycleOwner, Observer { filteredList ->
            filteredList?.let {
                coffeeAdapter.updateCoffeeList(it.toMutableList())
            }
        })

        coffeeViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        coffeeViewModel.loadCoffee()
    }



    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                coffeeViewModel.filterCoffeeList(newText ?: "")
                return true
            }
        })
    }
}








