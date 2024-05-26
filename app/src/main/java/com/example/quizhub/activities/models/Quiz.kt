package com.example.quizhub.activities.models


data class Quiz(
    var id: String = "",
    var title: String = "",
    var questions: MutableMap<String, Questions> = mutableMapOf()
)