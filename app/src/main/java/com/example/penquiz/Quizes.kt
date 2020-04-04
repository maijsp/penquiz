package com.google.firebase.quickstart.database.kotlin.models

// This class is refer to the database of each quiz

class Quizes {
    var num: Long? = 0;
    var title: String? = "";
//    var quizid: String? = "";

//    constructor() {
//        var num: Long? = 0;
//        var title: String? = "";
////        var quizid: String? = "";
//    }
//
//    constructor(quiznum: Long?, title: String?, quizid:String?) {
//        this.num = quiznum
//        this.title = title
////        this.quizid  = quizid
//    }

    fun getNumQuestion(): String {
        return "No. Questions ${num.toString()}"
    }
//    fun getQuizId(): String? {
//        return quizid
//    }
}
