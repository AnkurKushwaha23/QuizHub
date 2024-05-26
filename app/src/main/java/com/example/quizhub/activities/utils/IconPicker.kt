package com.example.quizhub.activities.utils

import com.example.quizhub.R

object IconPicker {
    val icons = arrayOf(
        R.drawable.a,
        R.drawable.h,
        R.drawable.b,
        R.drawable.g,
        R.drawable.d,
        R.drawable.e,
        R.drawable.c,
        R.drawable.f
    )
    var currentIconIndex = 0

    fun getIcon(): Int {
        currentIconIndex = (currentIconIndex + 1) % icons.size
        return icons[currentIconIndex]
    }
}