package com.example.coffeeapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.models.CategoryModel
import com.example.coffeeapp.models.CategoryModelClass
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CategoryViewModel : ViewModel() {

    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    val category: LiveData<MutableList<CategoryModel>> get() = _category
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCategory() {
        Firebase.firestore.collection("Category").document("type")
            .collection("CoffeeType").get()
            .addOnSuccessListener { questionData ->
                val questionsList = mutableListOf<CategoryModel>()
                for (data in questionData.documents) {
                    val question = data.toObject(CategoryModel::class.java)
                    question?.let { questionsList.add(it) }
                }
                _category.value = questionsList
            }.addOnFailureListener {
                _errorMessage.value = "Failed to fetch questions"
            }
    }}