package com.example.penquiz.ui.contactus

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.example.penquiz.R
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
    //private lateinit var contactViewModel: ContactViewModel
    private var googlemapView: MapView? = null
    private lateinit var auth: FirebaseAuth
    //private lateinit var database: DatabaseReference
    private lateinit var textFeedback: EditText
    private lateinit var postFeedback: Button

    /*--positiveButtonClick -> pass the Button text along with a Kotlin function that’s triggered when that button is clicked.
    The function is a part of the DialogInterface.OnClickListener() interface
    DialogInterface is an instance of the Dialog and Int is the id of the Button that is clicked.*/
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(activity?.applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }

    var database = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference = database.getReference()  //point to the root named "penquiz3d349"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        googlemapView?.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_contactus,container,false)

        googlemapView = root.findViewById(R.id.mapView)
        googlemapView?.onCreate(savedInstanceState)
        googlemapView?.getMapAsync(this);

        textFeedback = root.findViewById(R.id.text_feedback)
        postFeedback= root.findViewById(R.id.post_feedback)
        //database = FirebaseDatabase.getInstance().reference //point to the root named "penquiz3d349"
        //database = FirebaseDatabase.getInstance().getReference();
        postFeedback.setOnClickListener(this)

        return root
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
            textFeedback.setError(getString(R.string.Error_message));
            textFeedback.requestFocus();
            return;
        }

        myRef.child("Messages").child("sender ID").child(senderID.toString()).push().setValue(message)

        var builder = AlertDialog.Builder(this.context)
        // Set the alert dialog title
        builder.setTitle("THANK YOU")
        // Display a message on alert dialog
        builder.setMessage("Your message has been sent")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


