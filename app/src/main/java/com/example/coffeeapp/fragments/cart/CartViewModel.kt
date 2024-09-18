
package com.example.coffeeapp.fragments.cart
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartViewModel : ViewModel() {

    val cartItems = MutableLiveData<MutableList<CartModel>>(mutableListOf())
    private val firestore = FirebaseFirestore.getInstance()
    private val deliveryCharge = 200.0
    val subTotal = MutableLiveData<Double>()
    val totalAmount = MutableLiveData<Double>()

    fun fetchCartItems(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("cartItems")
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<CartModel>()
                for (document in result) {
                    val item = document.toObject(CartModel::class.java)
                    items.add(item)
                }
                cartItems.value = items
                calculateSubtotalAndTotal(items)
            }
            .addOnFailureListener {

            }
    }

    fun calculateSubtotalAndTotal(items: MutableList<CartModel>) {
        var subtotal = 0.0
        for (item in items) {
            subtotal += item.unitPrice * item.quantity
        }
        subTotal.value = subtotal
        totalAmount.value = subtotal + deliveryCharge
    }

    fun addItem(item: CartModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("cartItems")
            .add(item)
            .addOnSuccessListener {
                fetchCartItems(userId)
            }
            .addOnFailureListener {

            }
    }

    fun clearCart(userId: String) {
        val cartItemsRef = firestore.collection("users").document(userId).collection("cartItems")
        cartItemsRef.get().addOnSuccessListener { result ->
            val batch = firestore.batch()
            for (document in result.documents) {
                batch.delete(document.reference)
            }
            batch.commit().addOnSuccessListener {
                cartItems.value = mutableListOf()
            }
        }.addOnFailureListener {

        }
    }

    fun updateItem(item: CartModel) {
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
                fetchCartItems(userId)
            }
            .addOnFailureListener {

            }
    }

    fun deleteItem(item: CartModel) {
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

    fun addCartItemsToOrders(userId: String) {
        val cartItems = cartItems.value ?: mutableListOf()
        val ordersRef = firestore.collection("users")
            .document(userId)
            .collection("orders")

        val batch = firestore.batch()

        for (cartItem in cartItems) {
            val orderData = hashMapOf(
                "name" to cartItem.name,
                "quantity" to cartItem.quantity,
                "price" to cartItem.unitPrice * cartItem.quantity
            )
            val newOrderRef = ordersRef.document()
            batch.set(newOrderRef, orderData)
        }

        batch.commit()
            .addOnSuccessListener {
                clearCart(userId)
            }
            .addOnFailureListener { exception ->

            }
    }
}