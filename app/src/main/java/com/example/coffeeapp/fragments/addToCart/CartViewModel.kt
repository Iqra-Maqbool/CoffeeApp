
package com.example.coffeeapp.fragments.addToCart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartViewModel : ViewModel() {

    val cartItems = MutableLiveData<MutableList<CartModelClass>>(mutableListOf())
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchCartItems(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("cartItems")
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<CartModelClass>()
                for (document in result) {
                    val item = document.toObject(CartModelClass::class.java)
                    items.add(item)
                }
                cartItems.value = items
            }
            .addOnFailureListener {
                // Handle errors
            }
    }

    fun addItem(item: CartModelClass) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("cartItems")
            .add(item)
            .addOnSuccessListener {
                // Successfully added
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun clearCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val firestore = FirebaseFirestore.getInstance()
            val cartItemsRef = firestore.collection("users").document(user.uid).collection("cartItems")

            cartItemsRef.get().addOnSuccessListener { result ->
                val batch = firestore.batch()
                for (document in result.documents) {
                    batch.delete(document.reference)
                }
                batch.commit().addOnSuccessListener {
                    // Clear local cart items in ViewModel
                    cartItems.value = mutableListOf()
                }
            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }
        }
    }

    fun updateItem(item: CartModelClass) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cartRef = firestore.collection("users")
            .document(userId)
            .collection("cartItems")

        cartRef.whereEqualTo("name", item.name)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.update("quantity", item.quantity)
                }
            }
            .addOnFailureListener {
                // Handle errors
            }
    }

    fun deleteItem(item: CartModelClass) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cartRef = firestore.collection("users")
            .document(userId)
            .collection("cartItems")

        cartRef.whereEqualTo("name", item.name)
            .get()
            .addOnSuccessListener { result ->
                val batch = firestore.batch()
                for (document in result) {
                    batch.delete(document.reference)
                }
                batch.commit().addOnSuccessListener {

                    fetchCartItems(userId)
                }
                    .addOnFailureListener {

                    }
            }
    }
}
