package com.example.coffeeapp.fragments.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryViewModel : ViewModel() {

    private val _category = MutableLiveData<List<CategoryModelClass>>()
    val category: LiveData<List<CategoryModelClass>> get() = _category
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCategory() {
        Firebase.firestore.collection("Category")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { questionData ->
                val categoryList = mutableListOf<CategoryModelClass>()
                for (data in questionData.documents) {
                    val category = data.toObject(CategoryModelClass::class.java)
                    category?.let { categoryList.add(it) }
                }
                _category.value = categoryList


            }
            .addOnFailureListener {
                _errorMessage.value = "Failed to fetch categories"

            }
    }
}