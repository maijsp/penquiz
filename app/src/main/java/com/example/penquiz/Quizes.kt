package com.google.firebase.quickstart.database.kotlin.models

// This class is refer to the database of each quiz

class Quizes {
    var num: Long? = 0;
    var title: String? = "";

    constructor() {
        var num: Long? = 0;
        var title: String? = "";
    }

    constructor(quiznum: Long?, title: String?) {
        this.num = quiznum
        this.title = title
    }

    fun getNumQuestion(): String {
        return "No. Questions ${num.toString()}"
    }
}