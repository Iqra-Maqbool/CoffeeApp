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
import com.example.coffeeapp.viewmodels.CategoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var categoryList: ArrayList<CategoryModelClass>
  /*  private val viewModel: CategoryViewModel by viewModels()*/

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

      /*  initCategory()*/
        categoryList = ArrayList<CategoryModelClass>().apply{
            add(CategoryModelClass(R.drawable.mocha, "Caffe Mocha"))
            add(CategoryModelClass(R.drawable.white, "Flat White"))
            add(CategoryModelClass(R.drawable.mocha, "Caffe Mocha"))
            add(CategoryModelClass(R.drawable.white, "Flat White"))
            add(CategoryModelClass(R.drawable.mocha, "Caffe Mocha"))
            add(CategoryModelClass(R.drawable.white, "Flat White"))
            add(CategoryModelClass(R.drawable.mocha, "Caffe Mocha"))
            add(CategoryModelClass(R.drawable.white, "Flat White"))

        }
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = CategoryAdapter(categoryList,this@HomeActivity)
            setHasFixedSize(true)
        }


    }

  /*  private fun initCategory() {



        viewModel.category.observe(this, Observer{
            binding.CatRV.layoutManager=
                LinearLayoutManager(
                    this@HomeActivity,LinearLayoutManager.HORIZONTAL,false
                )
            binding.CatRV.adapter=CatAdapter(it)


        })
        viewModel.loadCategory()
    }*/
}