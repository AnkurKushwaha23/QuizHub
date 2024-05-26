package com.example.quizhub.activities.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizhub.R
import com.example.quizhub.activities.models.Questions

class OptionAdapter(val context: Context, val questions: Questions) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {
    private var options: List<String> = listOf(questions.option1,questions.option2,questions.option3,questions.option4)
    private var optionSelectedListener: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.option_item,parent,false)
        return OptionViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionView.text = options[position]
        holder.itemView.setOnClickListener {
            questions.userAnswer = options[position]
            optionSelectedListener?.invoke(true)
            notifyDataSetChanged()

        }
        if (questions.userAnswer == options[position]){
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected)
        }else{
            holder.itemView.setBackgroundResource(R.drawable.option_itembg)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class OptionViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var optionView = itemView.findViewById<TextView>(R.id.quiz_option)
    }

    fun setOnOptionSelectedListener(listener: (Boolean) -> Unit) {
        optionSelectedListener = listener
    }


}