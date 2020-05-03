package com.example.penquiz.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.R
import com.example.penquiz.adapter.HistoryAdapter
import com.example.penquiz.adapter.QuizAdapter
import com.example.penquiz.callback.QuizCallBack
import com.example.penquiz.callback.ResultCallBack
import com.example.penquiz.model.QuizResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.quickstart.database.kotlin.models.Quizes
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var resultRef: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var emptyImage:ImageView
    private lateinit var emptyText:TextView
    private lateinit var dataList : MutableList<QuizResult> // To keep Quizes object retrieve from database

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)
        user = FirebaseAuth.getInstance().currentUser!!

        dataList = ArrayList()
        historyAdapter = HistoryAdapter(dataList)

        // Initialization
        recyclerView = rootView.findViewById(R.id.recycleView_history)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        emptyImage = rootView.findViewById(R.id.empty)
        emptyText = rootView.findViewById(R.id.empty_description)

        fetchData(object: ResultCallBack {
            override fun onCallBack(quizes: List<QuizResult>) {
                dataList = quizes as MutableList<QuizResult>
                recyclerView.adapter = historyAdapter
                historyAdapter.notifyDataSetChanged()
                Log.d("FETCHING", "Successfully retreive history to dataList")
            }
        })

        return rootView
    }

    /**
     * @param callback
     * @author Saranphon Phaithoon
     * This function is used to retreive history from firebase
     */
    fun fetchData(callback: ResultCallBack) {
        // Fetching data from firebase
        database = FirebaseDatabase.getInstance()
        resultRef = database.getReference()
        resultRef.child("Users").child(user.uid).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.hasChild("history")) {
                    // get all children in this level
                    val children:Iterable<DataSnapshot> = dataSnapshot.child("history").children
                    // for each items
                    for (child: DataSnapshot in children) {
                        val result: QuizResult? = child.getValue(QuizResult::class.java)
                        result?.let { dataList.add(it) }
                        Log.d("fetching", "Title : ${result?.quiztitle} Score : ${result?.quizscore}")
                    }
                    callback.onCallBack(dataList)
                }
                else {
                    emptyImage.setBackgroundResource(R.drawable.emptybg)
                    emptyImage.visibility = View.VISIBLE
                    emptyText.visibility = View.VISIBLE
                }
            }
        })
    }
}