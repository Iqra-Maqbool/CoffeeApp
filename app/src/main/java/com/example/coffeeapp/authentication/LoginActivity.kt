package com.example.coffeeapp.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeapp.MainActivity
import com.example.coffeeapp.R
import com.example.coffeeapp.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.apply {
            loginBtn.setOnClickListener {
                val loginUsername = loginUsernameEt.text.toString()
                val loginPassword = loginPasswordEt.text.toString()
                if (loginUsername.isNotEmpty() && loginPassword.isNotEmpty()) {
                    loginUser(loginUsername, loginPassword)
                } else {
                    Toast.makeText(this@LoginActivity, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
                }
            }


            siguphere.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                finish()
            }
        }

    }


    //Login Function
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        firestore.collection("users").document(user.uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}



