package com.example.penquiz.callback

import com.example.penquiz.model.QuizResult

public interface ResultCallBack {
    fun onCallBack(quiz: List<QuizResult>)
}