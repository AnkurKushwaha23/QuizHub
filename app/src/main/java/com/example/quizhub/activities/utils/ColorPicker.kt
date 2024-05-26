package com.example.quizhub.activities.utils

object ColorPicker {
    val colors = arrayOf(
            "#8CC4F1",
            "#FFA07A",
            "#7FFF00",
            "#FFD700",
            "#40E0D0",
            "#DA70D6",
            "#6495ED"
        )
    var currentColorIndex = 0

    fun getColor(): String {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        return colors[currentColorIndex]
    }
}