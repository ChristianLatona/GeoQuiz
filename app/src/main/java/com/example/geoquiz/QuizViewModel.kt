package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG="QuizViewModel"

class QuizViewModel: ViewModel() {

    init {
        Log.d(TAG,"QuizViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"QuizViewModel instance about to be destroyed")
    }

     private val questionBank = listOf(//bad option to store model data, but app is very small
            Question(R.string.question_africa,false),
            Question(R.string.question_americas,true),
            Question(R.string.question_asia,true),
            Question(R.string.question_australia,true),
            Question(R.string.question_mideast,false),
            Question(R.string.question_oceans,true)
    )
    var currentIndex:Int=0
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResID

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    var currentQuestionAlreadyReplied: Boolean
        get() = questionBank[currentIndex].alreadyReplied
        set(value) {questionBank[currentIndex].alreadyReplied = value}

    var currentQuestionCheated: Boolean
        get() = questionBank[currentIndex].cheated
        set(value) {questionBank[currentIndex].cheated = value}

    val questionBankSize:Int
        get() = questionBank.size

    fun moveToNext(){
        if (currentIndex==questionBank.size-1){currentIndex=-1}
        currentIndex += 1 //% questionBank.size//il modulo non funziona
    }
    fun moveToPrevious(){
        if (currentIndex==0){currentIndex=questionBank.size}
        currentIndex -= 1 //% 2//il modulo non funziona
    }
}