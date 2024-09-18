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
import com.example.coffeeapp.databinding.FragmentHomeBinding
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coffeeViewModel: CoffeeViewModel
    private lateinit var coffeeAdapter: CoffeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }

        coffeeViewModel = ViewModelProvider(this)[CoffeeViewModel::class.java]
        setupCoffeeRecyclerView()
        observeCoffeeData()
        setupSearchView()

        return binding.root
    }

    private fun setupCoffeeRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    private fun observeCoffeeData() {
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

/*
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coffeeViewModel: CoffeeViewModel
    private lateinit var coffeeAdapter: CoffeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }

        coffeeViewModel = ViewModelProvider(this)[CoffeeViewModel::class.java]
        setupCoffeeRecyclerView()
        observeCoffeeData()
        setupSearchView()

        return binding.root
    }


    private fun setupCoffeeRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    private fun observeCoffeeData() {
        coffeeViewModel.filteredCoffeeList.observe(viewLifecycleOwner, Observer { coffeeList ->
            if (coffeeList != null) {
                coffeeAdapter = CoffeeAdapter(coffeeList, this@HomeFragment)
                binding.categoryRecyclerView.adapter = coffeeAdapter
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
*/






/*
package com.example.coffeeapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.databinding.FragmentHomeBinding

class com.example.coffeeapp.fragments.home.HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: CategoryViewModel
    private lateinit var coffeeViewModel: com.example.coffeeapp.fragments.home.CoffeeViewModel
    private lateinit var coffeeAdapter: CoffeeAdapter
    private lateinit var coffeeList: MutableList<CoffeeModelClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
        }


        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        coffeeViewModel = ViewModelProvider(this)[com.example.coffeeapp.fragments.home.CoffeeViewModel::class.java]
        initCategory()
        coffeeTypeToShow()
        setupSearchView()
        return binding.root
    }
    private fun coffeeTypeToShow() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }


        coffeeViewModel.category2.observe(viewLifecycleOwner, Observer { categoryList ->
            if (categoryList != null && categoryList.isNotEmpty()) {
                coffeeList = categoryList.toMutableList()
                coffeeAdapter = CoffeeAdapter(coffeeList, this@com.example.coffeeapp.fragments.home.HomeFragment)
                binding.categoryRecyclerView.adapter = coffeeAdapter
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCoffeeList(newText ?: "")
                return true
            }
        })
    }


    private fun filterCoffeeList(query: String) {
        val filteredList = if (query.isEmpty()) {
            coffeeList
        } else {
            coffeeList.filter {
                it.name?.contains(query, ignoreCase = true) ?: false
            }.toMutableList()
        }


        coffeeAdapter.updateCoffeeList(filteredList)
    }



    private fun initCategory() {
        binding.CatRV.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        viewModel.category.observe(viewLifecycleOwner, Observer { categoryList ->
            if (categoryList != null && categoryList.isNotEmpty()) {
                binding.CatRV.adapter = CategoryAdapter(categoryList)
            }
        })

        coffeeViewModel.loadCoffee()
        viewModel.loadCategory()
    }
}*/
