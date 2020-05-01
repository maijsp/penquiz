package com.example.penquiz.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.QuizActivity
import com.example.penquiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.quickstart.database.kotlin.models.Quizes


class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth // reretrieve database authentication
    private lateinit var quizesRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var rootView: View

    private lateinit var quizeList: ArrayList<Quizes> // to keep quiz data retrieve from firebase

    companion object {
        private const val TAG = "CheckDatabase"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // create the object quiz view inside the root view - recycleView_main
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        mRecyclerView = rootView.findViewById(R.id.recycleView_main)

        mAuth = FirebaseAuth.getInstance()
        var currentUserID = mAuth.currentUser

//        quizesRef = FirebaseDatabase.getInstance().getReference().child("Quizes")
//        println(quizesRef)
//
//        val options: FirebaseRecyclerOptions<Quizes> = FirebaseRecyclerOptions.Builder<Quizes>().setQuery(quizesRef, Quizes::class.java).build()
//        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Quizes, CustomViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
//                val itemview = LayoutInflater.from(parent.context).inflate(R.layout.quizes_row, parent, false)
//                return CustomViewHolder(itemview)
//            }
//            override fun onBindViewHolder(holder: CustomViewHolder, position: Int, quiz: Quizes) {
//                val refid = getRef(position).key.toString()
//                // Log.d("myTag", refid)
//                quizesRef.child(refid).addValueEventListener(object: ValueEventListener {
//                    override fun onCancelled(p0: DatabaseError) {
//                        Log.e(TAG, "Fail to load database")
//                    }
//                    override fun onDataChange(p0: DataSnapshot) {
//                        holder.quizname.setText(quiz.title)
//                        holder.quiznum.setText(quiz.getNumQuestion())
//                        println("$quiz_name and $quiz_detail")
//                    }
//                })
//            }
//        }
//        // set layout
//        mRecyclerView.layoutManager = LinearLayoutManager(activity)
//        // set adapter
//        mRecyclerView.adapter = firebaseRecyclerAdapter
//        firebaseRecyclerAdapter.startListening()

        // Fetching data from firebase
        database = FirebaseDatabase.getInstance()
        quizesRef = database.getReference()
        quizeList = ArrayList()
        quizesRef.child("Quizes").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // get all children in this level
                val children:Iterable<DataSnapshot> = dataSnapshot.children
                // for each items
                for (child:DataSnapshot in children) {
                    val quize: Quizes? = child.getValue(Quizes::class.java)
                    quizeList.add(quize!!)
                    Log.d("fetching", "${quize!!.title} ${quize!!.num}")
                }
                done(quizeList)
            }

        })


        return rootView
    }
    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var quizname: TextView = itemView!!.findViewById<TextView>(R.id.quiz_name)
        var quiznum: TextView = itemView!!.findViewById(R.id.quiz_detail)
        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "This is Toast example.+ ${quizname.text}", Toast.LENGTH_LONG).show();
                var intent = Intent(itemView.context, QuizActivity::class.java) // Create an Intent to QuizActivity activity
                var position = adapterPosition // get AdapterPositiion (where users click)
                intent.putExtra("quizname", quizname.text.toString()) // pass value of quizname to QuizActivity Intent
                intent.putExtra("position", position) // pass value of adapter position (where user click to QuizActivity Intent)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun done(k: ArrayList<Quizes>?) {
        Log.d("fetching", "result : ${k}}")
        if (k != null) {
            for (quiz:Quizes in k) {
                Log.d("fetching", "Quiz : ${quiz.title} and ${quiz.num}")
            }
        }
        Log.d("fetching", "Success retreieve database")
    }
}