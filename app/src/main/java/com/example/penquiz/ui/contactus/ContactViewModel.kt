package com.example.penquiz.ui.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Contact us"
    }
    val text: LiveData<String> = _text
}