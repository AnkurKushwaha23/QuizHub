package com.example.quizhub.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizhub.R
import com.example.quizhub.activities.Fragments.EditUser
import com.example.quizhub.activities.models.User
import com.example.quizhub.activities.utils.InternetCheck
import com.example.quizhub.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity(), EditUser.OnProfileUpdatedListener {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var uName: String
    private lateinit var gender: String

    companion object {
        private const val PREFS_NAME = "quizhub_prefs"
        private const val KEY_IS_PROFILE_UPDATED = "is_profile_updated"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
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

        binding.tvEmail.text = firebaseAuth.currentUser?.email

        // Retrieve user data from Firestore
        loadUserData()

        // Logout Button
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            with(sharedPreferences.edit()) {
                putBoolean(KEY_IS_PROFILE_UPDATED, false)
                apply()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun loadUserData() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            firestore.collection("USERS").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            binding.tvUserName.text = user.userName
                            uName = user.userName.toString()
                            gender = user.userGender!!
                            when (gender) {
                                "Male" -> binding.profileImage.setImageResource(R.drawable.profile)
                                "Female" -> binding.profileImage.setImageResource(R.drawable.profile2)
                                else -> binding.profileImage.setImageResource(R.drawable.profile3)
                            }

                            // btnEditProfile
                            binding.btnEditProfile.setOnClickListener {
                                val userToEdit = User(userName = uName, userGender = gender)
                                val editUser = EditUser.newInstance(userToEdit)
                                editUser.show(supportFragmentManager, "EditUserBottomSheet")
                            }
                        }
                    } else {
                        Log.d("ProfileActivity", "No such document")
                        Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileActivity", "get failed with ", exception)
                    Toast.makeText(this, "Failed to load user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProfileUpdated() {
        loadUserData()
    }
}
