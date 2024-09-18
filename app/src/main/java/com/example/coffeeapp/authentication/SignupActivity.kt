package com.example.coffeeapp.authentication
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeapp.R
import com.example.coffeeapp.databinding.ActivitySignupBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.registerBtn.setOnClickListener {
            binding.apply {
                val signupUsername = signupUsernameEt.text.toString()
                val signupEmail = signupEmailEt.text.toString()
                val signupPassword = signupPasswordEt.text.toString()
                if (signupUsername.isNotEmpty() && signupEmail.isNotEmpty() && signupPassword.isNotEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(signupEmail).matches()) {
                        signupUser(signupUsername, signupEmail, signupPassword)
                    } else {
                        Toast.makeText(this@SignupActivity, "Invalid Email Format", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Fill All Fields", Toast.LENGTH_SHORT).show()
                } }

        }


        binding.loginHere.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }


    private fun signupUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    if (user != null) {
                        val userModel = UserModel(username, email, password)
                        firestore.collection("users").document(user.uid).set(userModel)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
