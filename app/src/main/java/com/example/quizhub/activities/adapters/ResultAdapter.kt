package com.example.quizhub.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizhub.R
import com.example.quizhub.activities.models.Quiz
import com.example.quizhub.activities.models.Questions

class ResultAdapter(private val context: Context, private val quiz: Quiz) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private val questions: List<Questions> = quiz.questions.values.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.quiz_result_item, parent, false)
        return ResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val question = questions[position]
        holder.tvDescription.text = question.description
        holder.tvUserAnswer.text = "User Answer: ${question.userAnswer}"
        holder.tvAnswer.text = "Correct Answer: ${question.answer}"
        // Change the text color if the user's answer is incorrect
        val color = if (question.userAnswer == question.answer) {
            ContextCompat.getColor(context, R.color.correct_answer_color) // Define your color in colors.xml
        } else {
            ContextCompat.getColor(context, R.color.incorrect_answer_color) // Define your color in colors.xml
        }
        holder.tvUserAnswer.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvUserAnswer: TextView = itemView.findViewById(R.id.tvUserAnswer)
        val tvAnswer: TextView = itemView.findViewById(R.id.tvAnswer)
    }
}
