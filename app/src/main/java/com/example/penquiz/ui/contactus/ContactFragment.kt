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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class ContactFragment  : Fragment(), OnMapReadyCallback {
    private lateinit var contactViewModel: ContactViewModel
    private var googlemapView: MapView? = null

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



}

