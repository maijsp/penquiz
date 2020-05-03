package com.example.penquiz.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.penquiz.R

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
//            val quizname = arguments?.getString("quizname").toString()
//            val quizscore = arguments?.getInt("quizscore", 0)
//            Log.d("HISTORY", "${quizname} ${quizscore}")

        return root
    }
}