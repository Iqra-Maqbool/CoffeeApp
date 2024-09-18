package com.example.coffeeapp.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class CoffeeViewModel : ViewModel() {


    private val _coffeeList = MutableLiveData<List<CoffeeModel>?>()
    val coffeeList: LiveData<List<CoffeeModel>?> get() = _coffeeList

    private val _filteredCoffeeList = MutableLiveData<List<CoffeeModel>?>()
    val filteredCoffeeList: LiveData<List<CoffeeModel>?> get() = _filteredCoffeeList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCoffee() {
        Firebase.firestore.collection("Coffee")
            .document("type")
            .collection("CoffeeType")
            .get()
            .addOnSuccessListener { coffeeData ->
                val coffeeItems = mutableListOf<CoffeeModel>()
                for (data in coffeeData.documents) {
                    val coffee = data.toObject(CoffeeModel::class.java)
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
