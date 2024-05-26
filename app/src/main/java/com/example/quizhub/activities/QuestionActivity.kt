package com.example.quizhub.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizhub.R
import com.example.quizhub.activities.adapters.OptionAdapter
import com.example.quizhub.activities.models.Questions
import com.example.quizhub.activities.models.Quiz
import com.example.quizhub.databinding.ActivityQuestionBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.gson.Gson

class QuestionActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionBinding
    lateinit var firestore: FirebaseFirestore
    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String, Questions>? = null
    var index = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.appBar)
        setUpFireStore()
        setUpEventListener()
    }

    private fun setUpEventListener() {
        binding.btnPrevious.setOnClickListener {
            index--
            bindView()
        }
        binding.btnNext.setOnClickListener {
            index++
            bindView()
        }
        binding.btnSubmit.setOnClickListener {
            // Sort the questions map by keys
//            val sortedQuestions = quizzes!![0].questions.toSortedMap()
//            quizzes!![0].questions = sortedQuestions
//            Toast.makeText(this,questions.toString(),Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("QUIZ", json)
            startActivity(intent)
            finish()
        }

    }

    fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val date = intent.getStringExtra("DATE")
        supportActionBar?.title = "QuizHub : $date"
        if (date != null) {
            firestore.collection("QUIZZES").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {
                        quizzes = it.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindView()
//                        Log.d("DATA", it.toObjects(Quiz::class.java).toString())
                    }
                }
        }

    }

    private fun bindView() {
        binding.btnNext.visibility = View.GONE
        binding.btnPrevious.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE

        val question = questions!!["question$index"]
        question?.let {
            binding.description.text = it.description
            val optionAdapter = OptionAdapter(this, it)
            optionAdapter.setOnOptionSelectedListener { isOptionSelected ->
                updateButtonVisibility(isOptionSelected)
            }
            binding.optionList.layoutManager = LinearLayoutManager(this)
            binding.optionList.adapter = optionAdapter
            binding.optionList.setHasFixedSize(true)
        }
    }

    private fun updateButtonVisibility(isOptionSelected: Boolean) {
        if (index == 1) {
            binding.btnNext.visibility = if (isOptionSelected) View.VISIBLE else View.GONE
        } else if (index == questions!!.size) {
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE
        } else {
            binding.btnNext.visibility = if (isOptionSelected) View.VISIBLE else View.GONE
            binding.btnPrevious.visibility = View.VISIBLE
        }
    }
}