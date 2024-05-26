package com.example.quizhub.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizhub.R
import com.example.quizhub.activities.Fragments.EditUser
import com.example.quizhub.activities.adapters.QuizAdapter
import com.example.quizhub.activities.models.Quiz
import com.example.quizhub.activities.models.User
import com.example.quizhub.activities.utils.InternetCheck
import com.example.quizhub.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), EditUser.OnProfileUpdatedListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    lateinit var firestore: FirebaseFirestore
    private var quizList = mutableListOf<Quiz>()

    companion object {
        private const val PREFS_NAME = "quizhub_prefs"
        private const val KEY_IS_PROFILE_UPDATED = "is_profile_updated"
    }

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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


        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val isProfileUpdated = sharedPreferences.getBoolean(KEY_IS_PROFILE_UPDATED, false)
        if (!isProfileUpdated) {
            if(internetCheck.isInternetAvailable(this)){
                showEditUserBottomSheet()
            }
        }

        setUpView()


        // FCM
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "FCM Registration Token: $token")
        }

        // OnBackPressed
        val backcallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, backcallback)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("QuizHub")
            .setMessage("Do you want to exit?")
            .setPositiveButton("Yes") { dialog, _ ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun setUpView() {
        setUpFireStore()
        setUpDrawerLayout()
        setUpRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("QUIZZES")
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
            } else {
                quizList.clear()
                quizList.addAll(value.toObjects(Quiz::class.java))
                quizList.sortBy { extractNumericPart(it.title) }
                Log.d("MainActivity", "Quizzes fetched: ${quizList.size}") // Log fetched data size
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun extractNumericPart(title: String): Int {
        return title.substringAfter("Day-").toIntOrNull() ?: 0
    }


    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        binding.quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.quizRecyclerView.adapter = adapter
    }

    fun setUpDrawerLayout() {
        setSupportActionBar(binding.appBar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.main, R.string.app_name, R.string.app_name)
        actionBarDrawerToggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.userProfile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.followUs -> {
                    startActivity(Intent(this, ContactActivity::class.java))
                }
                R.id.rateUs -> {
                    // Handle rate us click
                    Toast.makeText(this, "This App is not available on PlayStore !!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other potential items
                }
            }
            // Close the navigation drawer
            binding.main.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEditUserBottomSheet() {
        val user = getCurrentUser() // Replace with the actual user retrieval logic
        val editUser = EditUser.newInstance(user)
        editUser.show(supportFragmentManager, editUser.tag)
    }

    private fun getCurrentUser(): User {
        // Replace with the actual logic to get the current user
        return User(userName = "User", userGender = "Male")
    }

    override fun onProfileUpdated() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_PROFILE_UPDATED, true)
            apply()
        }
    }
}
