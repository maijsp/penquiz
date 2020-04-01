package com.example.penquiz.ui.contactus

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.example.penquiz.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class ContactViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Contact us"
    }
    val text: LiveData<String> = _text
    private val mapFragment = SupportMapFragment()
}