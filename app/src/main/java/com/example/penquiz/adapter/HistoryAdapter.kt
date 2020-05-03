package com.example.penquiz.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.R
import com.example.penquiz.model.QuizResult
import kotlinx.android.synthetic.main.history_row.view.*

public class HistoryAdapter(internal var dataset: List<QuizResult>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // - replace the contents of the view with that element
        // we apply animation to views here
        fun bind(result: QuizResult) {
            itemView.apply {
                container_history.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)
                trophy_score.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)
                val nameTextView = findViewById<TextView>(R.id.quiz_name)
                val scoreTextView = findViewById<TextView>(R.id.quiz_score)
                val trophy = findViewById<ImageView>(R.id.trophy_score)
                nameTextView.text = result.quiztitle
                scoreTextView.text = "Your score : ${result.quizscore}% "
                when {
                    result.quizscore >= 80 -> trophy.setImageResource(R.drawable.a)
                    result.quizscore >= 70 -> trophy.setImageResource(R.drawable.b)
                    result.quizscore >= 60 -> trophy.setImageResource(R.drawable.c)
                    result.quizscore >= 50 -> trophy.setImageResource(R.drawable.d)
                    result.quizscore < 50 -> trophy.setImageResource(R.drawable.e)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.history_row, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return HistoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(dataset[position])
    }
}
