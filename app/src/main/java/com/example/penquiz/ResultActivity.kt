package com.example.penquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.penquiz.model.QuizResult
import com.example.penquiz.ui.history.HistoryFragment
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {

    private lateinit var user:FirebaseUser
    private lateinit var mDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // get data sent from QuizActivity
        val quizid: Int = intent.getIntExtra("quizid", 0)
        val score: Int = intent.getIntExtra("score", 0)
        val numQuestion: Int = intent.getIntExtra("numQuestion", 0)
        val quizname: String = intent.getStringExtra("quizname")!!
        Log.i("SCORE", "Score receivce  : ${score}") // will be used to keep in User History
        Log.i("SCORE", "Num question receivce  : ${numQuestion}")
        Log.i("SCORE", "Quiz Name : ${quizname}") // will be used to keep in User History
        Log.i("SCORE", "Quiz id : ${quizid}") // will be used to keep in User History

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

        // Get current user to keep the history
        user = FirebaseAuth.getInstance().currentUser!!
        keepHistory(result.toInt(), quizid, quizname)

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

    /**
     * @param score
     * @param quizid
     * @param quizname
     * @author Saranphon Phathoon
     * keepHistory(...) will update the history to keep in the firebase
     */
    fun keepHistory(score:Int, quizid:Int , quizname:String) {
        Log.d("RESULT", "Keep history ${user.uid} -> ${score} ${quizname}")
        mDatabase = FirebaseDatabase.getInstance().getReference("Users") // Reference to Users

        // Initialize QuizResult object to keep result in history
        val result:QuizResult = QuizResult()
        result.quizscore = score.toLong()
        result.quiztitle = quizname.toString()
        mDatabase.child(user.uid).child("history").child(quizid.toString()).setValue(result)
    }
}
