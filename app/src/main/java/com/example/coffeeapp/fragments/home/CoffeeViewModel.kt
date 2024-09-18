package com.example.coffeeapp.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class CoffeeViewModel : ViewModel() {


    private val _coffeeList = MutableLiveData<List<CoffeeModelClass>?>()
    val coffeeList: LiveData<List<CoffeeModelClass>?> get() = _coffeeList

    private val _filteredCoffeeList = MutableLiveData<List<CoffeeModelClass>?>()
    val filteredCoffeeList: LiveData<List<CoffeeModelClass>?> get() = _filteredCoffeeList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCoffee() {
        Firebase.firestore.collection("Coffee")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { coffeeData ->
                val coffeeItems = mutableListOf<CoffeeModelClass>()
                for (data in coffeeData.documents) {
                    val coffee = data.toObject(CoffeeModelClass::class.java)
                    coffee?.let {
                        coffeeItems.add(it)
                    }
                }
                _coffeeList.value = coffeeItems
                _filteredCoffeeList.value = coffeeItems
            }
            .addOnFailureListener {
                _errorMessage.value = "Failed to fetch coffee data"
            }
    }

    fun filterCoffeeList(query: String) {
        if (query.isEmpty()) {
            _filteredCoffeeList.value = _coffeeList.value
        } else {
            val filteredList = _coffeeList.value?.filter {
                it.name?.contains(query, ignoreCase = true) ?: false
            }

            _filteredCoffeeList.value = filteredList
        }
    }
}



/*
package com.example.coffeeapp.fragments.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class com.example.coffeeapp.fragments.home.CoffeeViewModel : ViewModel() {

    private val _category2= MutableLiveData<MutableList<CoffeeModelClass>>()
    val category2: LiveData<MutableList<CoffeeModelClass>> get() = _category2
    private val _errorMessage2 = MutableLiveData<String?>()
    val errorMessage2: LiveData<String?> get() = _errorMessage2

    fun loadCoffee() {
        Firebase.firestore.collection("Coffee")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { questionData ->
                val categoryList = mutableListOf<CoffeeModelClass>()
                for (data in questionData.documents) {
                    val category = data.toObject(CoffeeModelClass::class.java)
                    category?.let {
                        categoryList.add(it)
                    }
                }
                _category2.value = categoryList
            }
            .addOnFailureListener {
                _errorMessage2.value = "Failed to fetch categories"
            }
    }
}*/
