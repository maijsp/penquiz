package com.example.penquiz.ui.contactus

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.penquiz.R
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ContactFragment  : Fragment(), OnMapReadyCallback,View.OnClickListener {
    private lateinit var contactViewModel: ContactViewModel
    private var googlemapView: MapView? = null
    private lateinit var auth: FirebaseAuth
    //private lateinit var database: DatabaseReference
    private lateinit var textFeedback: EditText
    private lateinit var postFeedback: Button

    var data_base = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference = data_base.getReference()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        googlemapView?.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contactus,container,false)

        val textView: TextView = root.findViewById(R.id.text_contact)
        contactViewModel.text.observe(this, Observer {
            textView.text = it
        })

        googlemapView = root.findViewById(R.id.mapView)
        googlemapView?.onCreate(savedInstanceState)
        googlemapView?.getMapAsync(this);

        textFeedback = root.findViewById(R.id.text_feedback)
        postFeedback= root.findViewById(R.id.post_feedback)
        //database = FirebaseDatabase.getInstance().reference //point to the root named "penquiz3d349"
        //database = FirebaseDatabase.getInstance().getReference();
        postFeedback.setOnClickListener(this)

//        postFeedback.setOnClickListener(View.OnClickListener {
//            fun onClick(v: View){
//                val message: String = textFeedback.getText().toString()
//                if (TextUtils.isEmpty(message)){
//                    textFeedback.setError("Enter Your Message");
//                    textFeedback.requestFocus();
//                    return;
//                }
//                database.child("Messages").child("senderID").child(senderID.toString()).child(message).push()
//
//            }
//        })

        return root
//// Inflate the layout for this fragment
////return inflater.inflate(R.layout.fragment_contactus, container, false)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.addMarker(MarkerOptions().position(LatLng(13.793128, 100.323322)).title("Mahidol University"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.793128, 100.323322),15.0f))

    }

    override fun onResume() {
        super.onResume()
        googlemapView?.onResume()

    }

    override fun onPause() {
        super.onPause()
        googlemapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        googlemapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        googlemapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        googlemapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        googlemapView?.onLowMemory()
    }

    override fun onClick(v: View?) {
        val message: String = textFeedback.getText().toString()
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid
        if (TextUtils.isEmpty(message)){
            textFeedback.setError("Enter Your Message");
            textFeedback.requestFocus();
            return;
        }

        //database.child("Messages").child("senderID").child(senderID.toString()).child(message).push()
        myRef.child("Messages").child("sender ID").child(senderID.toString()).push().setValue(message)
    }




}

