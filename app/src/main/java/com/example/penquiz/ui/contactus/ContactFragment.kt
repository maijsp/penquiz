package com.example.penquiz.ui.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.penquiz.R

class ContactFragment  : Fragment(){
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contactus,container,false)
        val textView: TextView = root.findViewById(R.id.text_contact)
        contactViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
// Inflate the layout for this fragment
//return inflater.inflate(R.layout.fragment_contactus, container, false)
    }


}