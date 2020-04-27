package com.example.penquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.R.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.quickstart.database.kotlin.models.Quizes
import kotlinx.android.synthetic.main.quizes_row.*

class QuizActivity : AppCompatActivity() {
    private lateinit var questionRef: DatabaseReference
    private lateinit var countRef: DatabaseReference
    private lateinit var recyclerChoice: RecyclerView
    private var countQuestion = 0
    private var total = -1
    private var quizId = "0"
    private var score = 0
    private lateinit var button1:Button
    private lateinit var button2:Button
    private lateinit var button3:Button
    private lateinit var button4:Button
    private lateinit var buttonBack:Button
    private lateinit var buttonNext:Button
    private lateinit var textQuesTitle:TextView
    private lateinit var testQuesDesc:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_quiz)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title

        // Test passed data
        val quizname = intent?.getStringExtra("quizname")
        var text = findViewById(id.ques_title) as TextView
        actionbar!!.title = quizname.toString()

        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        quizId = intent.getIntExtra("position", 0)?.plus(1).toString()
        Toast.makeText(
            this,
            "Your position.+ ${intent?.getIntExtra("position", 0)?.plus(1)}",
            Toast.LENGTH_LONG
        ).show();

        // declare reference to button in activity_quiz.xml
        textQuesTitle = findViewById(id.ques_title)
        testQuesDesc = findViewById(id.ques_description)
        button1 = findViewById(id.choice1)
        button2 = findViewById(id.choice2)
        button3 = findViewById(id.choice3)
        button4 = findViewById(id.choice4)
//        buttonBack = findViewById(id.back)
//        buttonNext= findViewById(id.next)

        // count number of question
        countRef = FirebaseDatabase.getInstance().getReference().child("Quizes").child(quizId)
        countRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("myTag","Num question = " + dataSnapshot.child("num").value)
                countQuestion = dataSnapshot.child("num").value.toString().toInt()
            }
        }
        )

        updateQuestion()

    }

    /*
    This function is used to  update the question
     */
    fun updateQuestion() {
        textQuesTitle.text = "Question ${(total+2)}"
        if (total > countQuestion) {
            // go to results activity
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("result", total);
            }
            startActivity(intent)

        } else {
            total++;
            // go to next question
            // reference to the quizID
            questionRef = FirebaseDatabase.getInstance().getReference().child("Quizes").child(quizId).child("Questions").child(total.toString())
            Log.d("myTag", questionRef.toString())
            questionRef.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var questions: Questions? = dataSnapshot.getValue<Questions>()

                    // Assign value to the questions
                    testQuesDesc.text = questions!!.questionText
                    button1.text = questions.option_A  // Option A
                    button2.text = questions.option_B  // Option B
                    button3.text = questions.option_C  // Option C
                    button4.text = questions.option_D  // Option D
                    // Log.i("ques", questions!!.answer)

                    // When user click choice 1
                    button1.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                            if(button1.text.toString().equals(questions.answer)) {
                                button1.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                // if user click at correct answer
                                val handler:Handler = Handler()
                                handler.postDelayed(object: Runnable {
                                    @SuppressLint("ResourceAsColor")
                                    override fun run() {
                                        Log.i("Check", "Correct answer")
                                        // update the score
                                        score++;
                                        // change button to default for  new  question
                                        // button1.background.setColorFilter(resources.getColor(R.color.lightgray), PorterDuff.Mode.SRC_IN)
                                        button1.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion()
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Check", "Wrong answer")
                                button1.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button2.text.toString().equals(questions.answer)){
                                    button2.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button3.text.toString().equals(questions.answer)){
                                    button3.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button4.text.toString().equals(questions.answer)){
                                    button4.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                var handler:Handler = Handler()
                                handler.postDelayed( {
                                    Log.d("RESET Tag","reset all button");
                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion()
                                },1500)
                            }
                        }

                    })
                    // When user click choice 2
                    button2.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                            if(button2.text.toString().equals(questions.answer)) {
                                // if user click at correct answer
                                button2.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                val handler:Handler = Handler()
                                handler.postDelayed(object: Runnable {
                                    @SuppressLint("ResourceAsColor")
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default for new question
                                        button2.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion()
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Wrong", "Wrong answer")
                                button2.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button1.text.toString().equals(questions.answer)){
                                    button1.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button3.text.toString().equals(questions.answer)){
                                    button3.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button4.text.toString().equals(questions.answer)){
                                    button4.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                var handler:Handler = Handler()
                                handler.postDelayed({
                                    Log.d("RESET Tag","reset all button");
                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion()
                                },1500)
                            }
                        }

                    })
                    // When use click choice 3
                    button3.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                            if(button3.text.toString().equals(questions.answer)) {
                                button3.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                // if user click at correct answer
                                val handler:Handler = Handler()
                                handler.postDelayed(object: Runnable {
                                    @SuppressLint("ResourceAsColor")
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default for new question
                                        button3.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion()
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Wrong", "Wrong answer")
                                button3.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button1.text.toString().equals(questions.answer)){
                                    button1.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button2.text.toString().equals(questions.answer)){
                                    button2.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button4.text.toString().equals(questions.answer))
                                    button4.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                var handler:Handler = Handler()
                                handler.postDelayed({
                                    Log.d("RESET Tag","reset all button");
                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion()
                                },1500)

                        }

                    })
                    // when user click choice 4
                    button4.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                            if(button4.text.toString().equals(questions.answer)) {
                                button4.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                // if user click at correct answer
                                val handler:Handler = Handler()
                                handler.postDelayed(object: Runnable {
                                    @SuppressLint("ResourceAsColor")
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default fot new question
                                        button4.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion()
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Wrong", "Wrong answer")
                                button4.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button1.text.toString().equals(questions.answer)){
                                    button1.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button2.text.toString().equals(questions.answer)){
                                    button2.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                else if(button3.text.toString().equals(questions.answer)){
                                    button3.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                }
                                var handler:Handler = Handler()
                                handler.postDelayed({
                                    Log.d("RESET Tag","reset all button");
                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion()
                                },1500)
                            }
                        }

                    })
                    // End of question checking
                }
            })
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
