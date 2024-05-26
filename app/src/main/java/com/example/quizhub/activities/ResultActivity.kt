package com.example.quizhub.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizhub.R
import com.example.quizhub.activities.adapters.ResultAdapter
import com.example.quizhub.activities.models.Questions
import com.example.quizhub.activities.models.Quiz
import com.google.gson.Gson

class ResultActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var quizData : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setUpViews()
    }

    private fun setUpViews() {
        quizData = intent.getStringExtra("QUIZ")!!
        quiz = Gson().fromJson<Quiz>(quizData,Quiz::class.java)
        calculateScore()
        setAnswerView()
    }

    private fun setAnswerView() {
        val recyclerView: RecyclerView = findViewById(R.id.resultRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ResultAdapter(this,quiz)

//        val builder = StringBuilder("")
//        for (entry in quiz.questions.entries){
//            val question = entry.value
//
//            builder.append("<font color='#000000'><b>Questions: ${question.description}</b><font/>\n\n")
//            builder.append("<font color='#009688'><b>Answer: ${question.answer}</b><font/>\n\n")
//        }
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            tvAnswer.text = Html.fromHtml(builder.toString(),Html.FROM_HTML_MODE_COMPACT);
//        }else{
//            tvAnswer.text = Html.fromHtml(builder.toString());
//        }
    }

    private fun calculateScore() {
        var score = 0
        var positiveAnswer = 0
        var negativeAnswer = 0
        for (entry in quiz.questions.entries){
            val question = entry.value
            if (question.answer == question.userAnswer){
                score += 10
                positiveAnswer += 1
            }else{
                negativeAnswer += 1
            }
        }
        val result = findViewById<TextView>(R.id.tvResult)
        val positive = findViewById<TextView>(R.id.tvPositive)
        val negative = findViewById<TextView>(R.id.tvNegative)
        result.text = "Your Score : $score"
        positive.text = "Total Right Answer : $positiveAnswer"
        negative.text = "Total Wrong Answer : $negativeAnswer"
    }
}