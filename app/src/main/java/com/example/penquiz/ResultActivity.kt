package com.example.penquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.nav_header_main.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var score: Int = intent.getIntExtra("score", 0)
        var numQuestion: Int = intent.getIntExtra("numQuestion", 0)
        Log.i("SCORE", "Score receivce  : ${score}")
        Log.i("SCORE", "Num question receivce  : ${numQuestion}")

        var scoreTextView = findViewById<TextView>(R.id.score)
        scoreTextView.setText("${score}" + " / " + "${numQuestion}")
    }

    fun setScoreDescription(score: Int, numQuestion: Int) {

    }
}
