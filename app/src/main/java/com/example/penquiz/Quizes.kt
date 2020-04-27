package com.google.firebase.quickstart.database.kotlin.models

// This class is refer to the database of each quiz

class Quizes {
    var num: Long? = 0;
    var title: String? = "";

    fun getNumQuestion(): String {
        return "No. Questions ${num.toString()}"
    }
}
