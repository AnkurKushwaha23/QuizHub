package com.example.quizhub.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizhub.R
import com.example.quizhub.activities.Fragments.BottomSheetFragment
import com.example.quizhub.activities.utils.InternetCheck
import com.example.quizhub.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.btnLogin.setOnClickListener {
            if (!internetCheck.isInternetAvailable(this)) {
                Toast.makeText(this,"Internet is Unavailable !!",Toast.LENGTH_SHORT).show()
            }
            logIn()
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun logIn() {
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "LogIn Successfully !!", Toast.LENGTH_SHORT).show()
                    } else {
                        if (task.exception is FirebaseAuthInvalidUserException || task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("LoginError", task.exception.toString())
                        }
                    }
                }
        } else {
            Toast.makeText(this, "Please fill all Fields", Toast.LENGTH_SHORT).show()
        }
    }
}