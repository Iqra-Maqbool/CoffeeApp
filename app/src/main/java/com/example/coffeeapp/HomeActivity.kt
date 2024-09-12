package com.example.coffeeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeapp.adapter.CatAdapter
import com.example.coffeeapp.adapter.CategoryAdapter
import com.example.coffeeapp.databinding.ActivityHomeBinding
import com.example.coffeeapp.models.CategoryModelClass
import com.example.coffeeapp.viewmodels.Category2ViewModel
import com.example.coffeeapp.viewmodels.CategoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var categoryList: ArrayList<CategoryModelClass>
    private val viewModel: CategoryViewModel by viewModels()
    private val coffeeViewModel:Category2ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCategory()
        coffeeTypeToShow()

    }

    private fun coffeeTypeToShow() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            setHasFixedSize(true)
        }
        coffeeViewModel.category2.observe(this, Observer { categoryList ->
            if (categoryList != null && categoryList.isNotEmpty()) {
                binding.categoryRecyclerView.adapter = CategoryAdapter(categoryList, this@HomeActivity)
            }
        })
    }



    private fun initCategory() {

        binding.CatRV.layoutManager = LinearLayoutManager(
            this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
        )

        viewModel.category.observe(this, Observer { categoryList ->

            if (categoryList != null && categoryList.isNotEmpty()) {
                binding.CatRV.adapter = CatAdapter(categoryList)
            } else {

            }
        })
        coffeeViewModel.loadCoffee()
        viewModel.loadCategory()
    }

}