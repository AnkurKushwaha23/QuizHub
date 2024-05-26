package com.example.quizhub.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.HandlerThread
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizhub.R
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Handler

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        Thread.sleep(1000)
//        installSplashScreen()
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtVer: TextView = findViewById(R.id.tvVersion)
        var versionName: String? = null

        try {
            versionName = applicationContext.packageManager
                .getPackageInfo(applicationContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        txtVer.text = "VERSION $versionName"

        android.os.Handler(Looper.getMainLooper()).postDelayed({

            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // User is signed in, start MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Optional: Close the SplashActivity
            } else {
                // User is not signed in, start LoginActivity
                startActivity(Intent(this, LoginIntro::class.java))
                finish() // Optional: Close the SplashActivity
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }, 3000)

    }
}