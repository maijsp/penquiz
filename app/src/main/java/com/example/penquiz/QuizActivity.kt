package com.example.penquiz

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.daimajia.numberprogressbar.NumberProgressBar
import com.example.penquiz.R.id
import com.example.penquiz.R.layout
import com.example.penquiz.callback.MyCallback
import com.example.penquiz.model.Questions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_quiz.*


class QuizActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var questionRef: DatabaseReference
    private lateinit var countRef: DatabaseReference
    private lateinit var recyclerChoice: RecyclerView
    private var countQuestion = 0
    private var total = -1
    private var score = 0
    private var progressnum = 0

    private lateinit var button1:Button
    private lateinit var button2:Button
    private lateinit var button3:Button
    private lateinit var button4:Button
    private lateinit var textQuesTitle:TextView
    private lateinit var testQuesDesc:TextView
    private lateinit var progressBar: RoundCornerProgressBar
    private lateinit var quiztitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_quiz)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title

        // Receive data
        val intent = intent
        quiztitle = intent.getStringExtra("quizname")
        var quizId = intent.getIntExtra("quizid", -1)
        // Log.d("RECEIVE", quizName)

        // Test passed data
        actionbar!!.title = quiztitle
        Log.d("RECEIVE", "RECEIVE ${quizId} ${quiztitle}")

        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        // Toast.makeText(this, "Your quizid.+ ${quizId}", Toast.LENGTH_LONG).show();

        // declare reference to button in activity_quiz.xml
        textQuesTitle = findViewById(id.ques_title)
        testQuesDesc = findViewById(id.ques_description)
        button1 = findViewById(id.choice1)
        button2 = findViewById(id.choice2)
        button3 = findViewById(id.choice3)
        button4 = findViewById(id.choice4)
        progressBar = findViewById(id.progressbar)

        // call the callback object to get number of question
        readData(object: MyCallback {
            override fun onCallback(value: String) {
                countQuestion = value.toInt() // change countQuestion from String to Int
                updateQuestion(quizId) // to update question
            }
        }, quizId)
    }

    /**
     * Read the value from onDataChange() -- Asynchronous function
     * @param myCallback : callback interface to get value from onDataChange()
     * @param id : quizId
     */
    fun readData(myCallback: MyCallback, id: Int) {
        // count number of question
        countRef = FirebaseDatabase.getInstance().getReference().child("Quizes").child(id.toString())
        Log.d("QUIZ", "${countRef}")
        countRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val numQuestion = dataSnapshot.child("num").value.toString() // get the value of key "num"
                Log.d("INTENT", "Num Question = ${numQuestion}") // to get number of questions
                myCallback.onCallback(numQuestion) // to send the value to outside OnDataChange : It's asynchronous
            }
        })
    }

    /**
     * updateQuestion() is used to update the question of the quiz
     */
    fun updateQuestion(id:Int) {
        total++;
        progressBar.max = countQuestion.toFloat()
        progressBar.progress = total.toFloat()
        if (total == countQuestion) {
            // go to results activity
            Log.d("INTENT", "go to result activity ${total} ${countQuestion}")
            val intent = Intent(this, ResultActivity::class.java) // create a new intent
            intent.putExtra("score", score) // put the score to the result activity
            intent.putExtra("numQuestion", countQuestion)
            intent.putExtra("quizname", quiztitle)
            Log.d("SENDING", "${score} : ${countQuestion} : ${quiztitle}")
            startActivity(intent)

        } else {
            Log.d("INTENT", "Update question ${total} of ${countQuestion}") // To check question counter
            textQuesTitle.text = "Question ${(total+1)}"
            // go to next question
            // reference to the quizID
            questionRef = FirebaseDatabase.getInstance().getReference().child("Quizes").child(id.toString()).child("Questions").child(total.toString()) // To find the path to FirebaseDatabase of quiz
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
                                    override fun run() {
                                        Log.i("Check", "Correct answer")
                                        // update the score
                                        score++;
                                        // change button to default for new question
                                        button1.background.colorFilter = null
                                        button1.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion(id)
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Check", "Wrong answer")
                                button1.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button2.text.toString().equals(questions.answer)){
                                    // var drawable = ContextCompat.getDrawable(context, R.drawable.custom_button)
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
                                    Log.d("RESET","reset all button");
                                    button1.background.colorFilter = null
                                    button2.background.colorFilter = null
                                    button3.background.colorFilter = null
                                    button4.background.colorFilter = null

                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion(id)
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
                                    //m@SuppressLint("ResourceAsColor")
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default for new question
                                        button2.background.colorFilter = null
                                        button2.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion(id)
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
                                    button1.background.colorFilter = null
                                    button2.background.colorFilter = null
                                    button3.background.colorFilter = null
                                    button4.background.colorFilter = null

                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion(id)
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
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default for new question
                                        button3.background.colorFilter = null
                                        button3.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion(id)
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
                                    button1.background.colorFilter = null
                                    button2.background.colorFilter = null
                                    button3.background.colorFilter = null
                                    button4.background.colorFilter = null

                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion(id)
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
                                    override fun run() {
                                        // update the score
                                        score++;
                                        // change button to default fot new question
                                        button4.background.colorFilter = null
                                        button4.setBackgroundResource(R.drawable.custom_button)
                                        updateQuestion(id)
                                    }
                                },1500)
                            }
                            else {
                                // if use click wrong answer
                                Log.i("Wrong", "Wrong answer")
                                button4.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                if(button1.text.toString().equals(questions.answer)){
                                    // button1.background.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
                                    var drawable = ResourcesCompat.getDrawable(resources, R.drawable.custom_button, null)
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
                                    button1.background.colorFilter = null
                                    button2.background.colorFilter = null
                                    button3.background.colorFilter = null
                                    button4.background.colorFilter = null

                                    button1.setBackgroundResource(R.drawable.custom_button)
                                    button2.setBackgroundResource(R.drawable.custom_button)
                                    button3.setBackgroundResource(R.drawable.custom_button)
                                    button4.setBackgroundResource(R.drawable.custom_button)
                                    updateQuestion(id)
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
