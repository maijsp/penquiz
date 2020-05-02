package com.example.penquiz.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.penquiz.R
import com.example.penquiz.adapter.QuizAdapter
import com.example.penquiz.callback.QuizCallBack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.quickstart.database.kotlin.models.Quizes
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth // reretrieve database authentication
    private lateinit var quizesRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var rootView: View
    private lateinit var searchInput: EditText

    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView

    private lateinit var dataList : MutableList<Quizes> // To keep Quizes object retrieve from database

    companion object {
        private const val TAG = "HOME"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Init rootView
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Init dataList
        dataList = ArrayList()

        // Retreive data from firebase https://www.youtube.com/watch?v=OvDZVV5CbQg to dataList
        fetchData(object: QuizCallBack{
            override fun onCallBack(quizes: List<Quizes>) {
                dataList = quizes as MutableList<Quizes>
                quizAdapter = QuizAdapter(dataList)
                recyclerView.adapter = quizAdapter
                Log.d("fetching", "Successfully retreive data to dataList")
            }
        })
        // Init view from root view
        recyclerView = rootView.findViewById(R.id.recycleView_main)
        searchInput = rootView.findViewById(R.id.searchtext)

        // set layout
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // set adapter corresponding to the search query
        searchInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // perform filter when text changed
                Log.d("CHECK", "textchange")
                quizAdapter.filter.filter(s)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        return rootView
    }

    /**
     * fetchData is used to get Quizes object from firebase
     * @param quizCallBack the callback to get value one onDataChange performed
     */
    fun fetchData(quizCallBack: QuizCallBack) {
        // Fetching data from firebase
        database = FirebaseDatabase.getInstance()
        quizesRef = database.getReference()
        quizesRef.child("Quizes").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // get all children in this level
                var children:Iterable<DataSnapshot> = dataSnapshot.children
                // for each items
                for (child:DataSnapshot in children) {
                    val quize: Quizes? = child.getValue(Quizes::class.java)
                    quize?.let { dataList.add(it) }
                    Log.d("fetching", "Title : ${quize?.title} Num : ${quize?.num} QuizID : ${quize?.quizid}")
                }
                // recyclerView.adapter = quizAdapter
                quizCallBack.onCallBack(dataList)
            }
        })
    }
}