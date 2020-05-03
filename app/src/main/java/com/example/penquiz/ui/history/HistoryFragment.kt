package com.example.penquiz.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.penquiz.R

class HistoryFragment : Fragment() {
    companion object {
        fun newInstance(name: String): HistoryFragment {
            val args = Bundle()
            args.putString("name", name)
            Log.d("HIStORY", "put $name")
            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
            val quizname = arguments?.getString("quizname").toString()
            val quizscore = arguments?.getInt("quizscore", 0)
        val name = arguments?.getString("name").toString()

        Log.d("HISTORY", "${quizname} ${quizscore} ${name}")

        return root
    }
}