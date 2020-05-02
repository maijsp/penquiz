package com.example.penquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlin.math.round
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // get data sent from QuizActivity
        var score: Int = intent.getIntExtra("score", 0)
        var numQuestion: Int = intent.getIntExtra("numQuestion", 0)
        Log.i("SCORE", "Score receivce  : ${score}")
        Log.i("SCORE", "Num question receivce  : ${numQuestion}")

        // Get views
        var scoreTextView = findViewById<TextView>(R.id.score)
        var correct = findViewById<TextView>(R.id.numcorrect)
        var incorrect = findViewById<TextView>(R.id.numincorrect)
        var backbutton = findViewById<Button>(R.id.backtomain)

        // Assign value to views
        correct.text = "$score"
        incorrect.text = "${numQuestion-score}"
        var result = (score.toDouble()/numQuestion.toDouble())*100
        scoreTextView.text = "${result.toInt()}%"

        // add OnClickListener to back button
        backbutton.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        })

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
        circularProgressBar.onProgressChangeListener = { progress ->
            // Do something
        }

        circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->
            // Do something
        }
    }
    fun setScoreDescription(score: Int, numQuestion: Int) {

    }
}
