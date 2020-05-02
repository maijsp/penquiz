package com.example.penquiz.callback

import com.google.firebase.quickstart.database.kotlin.models.Quizes

interface QuizCallBack {
    fun onCallBack(quiz:List<Quizes>)
}