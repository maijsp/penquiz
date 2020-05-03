package com.example.penquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.penquiz.ui.history.HistoryFragment
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.quizes_row.*
import kotlin.math.round
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // get data sent from QuizActivity
        val score: Int = intent.getIntExtra("score", 0)
        val numQuestion: Int = intent.getIntExtra("numQuestion", 0)
        val quizname: String = intent.getStringExtra("quizname")!!
        Log.i("SCORE", "Score receivce  : ${score}")
        Log.i("SCORE", "Num question receivce  : ${numQuestion}")
        Log.i("SCORE", "Quiz Name : ${quizname}")

        // Get views
        val scoreTextView = findViewById<TextView>(R.id.score)
        val correct = findViewById<TextView>(R.id.numcorrect)
        val incorrect = findViewById<TextView>(R.id.numincorrect)
        val backbutton = findViewById<Button>(R.id.backtomain)

        // Assign value to views
        correct.text = "$score"
        incorrect.text = "${numQuestion-score}"
        val result = (score.toDouble()/numQuestion.toDouble())*100
        scoreTextView.text = "${result.toInt()}%"

        // add OnClickListener to back button
        backbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }

        // Create bundle and send score to keep in HistoryFragment
//        val bundle: Bundle = Bundle()
//        bundle.putString("quizname", quizname)
//        bundle.putInt("quizscore", score)
//        val fragment:HistoryFragment = HistoryFragment()
//        fragment.arguments = bundle

        // Create circular progress bar
        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circle)
        circularProgressBar.apply {
            // Set Progress
            progress = 0f
            // or with animation
            setProgressWithAnimation(score.toFloat(), 1000) // =1s

            // Set Progress Max
            progressMax = numQuestion.toFloat()

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.RED
            progressBarColorEnd = resources.getColor(R.color.orange)
            progressBarColorDirection = CircularProgressBar.GradientDirection.LEFT_TO_RIGHT

            // Set background ProgressBar Color
            backgroundProgressBarColor = resources.getColor(R.color.lightpink)
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = resources.getColor(R.color.lightpink)
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }
}
