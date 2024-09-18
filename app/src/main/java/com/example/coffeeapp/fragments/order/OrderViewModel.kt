
package com.example.coffeeapp.fragments.order
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderViewModel : ViewModel() {

        private val firestore = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        private val _orders = MutableLiveData<List<OrderModel>>()
        val orders: LiveData<List<OrderModel>> get() = _orders

        private val _error = MutableLiveData<String>()
        val error: LiveData<String> get() = _error

        fun fetchOrders() {
            val user = auth.currentUser
            user?.let {
                firestore.collection("users")
                    .document(it.uid)
                    .collection("orders")
                    .get()
                    .addOnSuccessListener { documents ->
                        val orderList = mutableListOf<OrderModel>()
                        for (document in documents) {
                            val order = document.toObject(OrderModel::class.java)
                            orderList.add(order)
                        }
                        _orders.value = orderList
                    }
                    .addOnFailureListener {
                        _error.value = "Error"
                    }
            } ?: run {
                _error.value = "User is  not logged in."
            }
        }
    }
