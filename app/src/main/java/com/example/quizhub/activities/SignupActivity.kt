package com.example.quizhub.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizhub.R
import com.example.quizhub.activities.utils.InternetCheck
import com.example.quizhub.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create an instance of InternetCheck
        val internetCheck = InternetCheck()

        // Check internet connection and show BottomSheet if offline
        internetCheck.checkInternetAndShowBottomSheet(this, this)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnSignup.setOnClickListener {
            if (!internetCheck.isInternetAvailable(this)) {
                Toast.makeText(this,"Internet is Unavailable !!",Toast.LENGTH_SHORT).show()
            }
            signUp()
        }
    }
    private fun signUp() {
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this, "Registered Successfully !!", Toast.LENGTH_SHORT).show()
                        } else {
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.d("SignUpError", task.exception.toString())
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all Fields", Toast.LENGTH_SHORT).show()
        }
    }
}