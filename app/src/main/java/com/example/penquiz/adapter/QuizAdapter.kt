package com.example.penquiz.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.QuizActivity
import com.example.penquiz.R
import com.google.firebase.quickstart.database.kotlin.models.Quizes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.quizes_row.view.*
import java.util.*
import kotlin.collections.ArrayList

public class QuizAdapter(internal var dataset: List<Quizes>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>(), Filterable {
    // Initialize variables
    internal var myDatasetFilter:List<Quizes>
    init {
        this.myDatasetFilter = dataset
    }

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // - replace the contents of the view with that element
        // we apply animation to views here
        fun bind(quiz: Quizes) {
            itemView.apply {
                container.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)
                var image:ImageView = findViewById(R.id.quizimage)
                Picasso.get().load(quiz.imageURL).into(image)
                quiz_name.text = quiz.title
                quiz_detail.text = "Number of Question : ${quiz.num.toString()}"
                itemView.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(v: View?) {
                        Log.d("CLICKED", "quiz ${quiz.quizid}")
                        val intent = Intent(context, QuizActivity::class.java) // create a new intent to QuizActivity
                        intent.putExtra("quizid", quiz.quizid?.toInt())
                        intent.putExtra("quizname", quiz.title.toString())
                        Log.d("CLICKED", "Sending ${quiz.quizid} ${quiz.title}")
                        v!!.context.startActivity(intent)
                    }
                })
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        // create a new view
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.quizes_row, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return QuizViewHolder(layout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        // - get element from your dataset at this position
        Log.d("myTag", Arrays.toString(myDatasetFilter.toTypedArray()))
        holder.bind(myDatasetFilter[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDatasetFilter.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val key: String = constraint.toString()
                if(key.isEmpty()) {
                    myDatasetFilter = dataset
                }
                else {
                    val listFiltered: ArrayList<Quizes> = ArrayList()
                    for (row:Quizes in dataset) {
                        if(row.title!!.toLowerCase().contains(key.toLowerCase())) {
                            listFiltered.add(row)
                        }
                    }
                    myDatasetFilter = listFiltered
                }
                val filterRes: FilterResults = Filter.FilterResults()
                filterRes.values = myDatasetFilter
                return filterRes
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                myDatasetFilter = results?.values as List<Quizes>
                notifyDataSetChanged()
            }
        }
    }
    public interface QuizListener{
        fun onQuizClick(position: Int)
    }
}