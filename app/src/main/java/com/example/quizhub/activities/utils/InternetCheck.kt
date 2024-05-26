package com.example.quizhub.activities.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.FragmentActivity
import com.example.quizhub.activities.Fragments.BottomSheetFragment

class InternetCheck {

    @SuppressLint("ServiceCast")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun checkInternetAndShowBottomSheet(context: Context, activity: FragmentActivity) {
        if (!isInternetAvailable(context)) {
            val noInternetBottomSheet = BottomSheetFragment()
            noInternetBottomSheet.show(activity.supportFragmentManager, noInternetBottomSheet.tag)
        }
    }
}


