package com.example.coffeeapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.models.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryViewModel : ViewModel() {

    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    val category: LiveData<MutableList<CategoryModel>> get() = _category
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCategory() {
        Firebase.firestore.collection("Category")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { questionData ->
                val categoryList = mutableListOf<CategoryModel>()
                for (data in questionData.documents) {
                    val category = data.toObject(CategoryModel::class.java)
                    category?.let { categoryList.add(it) }
                }
                _category.value = categoryList


                if (categoryList.isNotEmpty()) {
                    Log.d("CategoryViewModel", "Fetched categories: ${categoryList.size}")
                } else {
                    Log.d("CategoryViewModel", "No categories found in Firestore")
                }
            }
            .addOnFailureListener {
                _errorMessage.value = "Failed to fetch categories"
                Log.e("CategoryViewModel", "Failed to fetch categories", it)
            }
    }
}