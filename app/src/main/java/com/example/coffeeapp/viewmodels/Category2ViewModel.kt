package com.example.coffeeapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.models.CategoryModelClass
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Category2ViewModel : ViewModel() {

    private val _category2= MutableLiveData<MutableList<CategoryModelClass>>()
    val category2: LiveData<MutableList<CategoryModelClass>> get() = _category2
    private val _errorMessage2 = MutableLiveData<String?>()
    val errorMessage2: LiveData<String?> get() = _errorMessage2

    fun loadCoffee() {
        Firebase.firestore.collection("Coffee")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { questionData ->
                val categoryList = mutableListOf<CategoryModelClass>()
                for (data in questionData.documents) {
                    val category = data.toObject(CategoryModelClass::class.java)
                    category?.let { categoryList.add(it) }
                }
                _category2.value = categoryList

                if (categoryList.isNotEmpty()) {
                    Log.d("CategoryViewModel", "Fetched categories: ${categoryList.size}")
                } else {
                    Log.d("CategoryViewModel", "No categories found in Firestore")
                }
            }
            .addOnFailureListener {
                _errorMessage2.value = "Failed to fetch categories"
                Log.e("CategoryViewModel", "Failed to fetch categories", it)
            }
    }
}