package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

// private const val TAG="MainActivity"
private const val KEY_INDEX="index"
private const val KEY_CHEAT_COUNTER="cheat_counter"
private const val REQUEST_CODE_CHEAT=0

class MainActivity : AppCompatActivity(){
    //@suppressLint("RestrictedApi")
    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var previousButton: Button
    private lateinit var cheatButton: Button
    private var repliedCounter:Int=0
    private var percentageScore:Float=0f
    private var cheatCounter:Int=0

    private val quizViewModel:QuizViewModel by lazy { ViewModelProvider(this).get(QuizViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex=savedInstanceState?.getInt(KEY_INDEX,0) ?: 0
        quizViewModel.currentIndex=currentIndex

        //val provider:ViewModelProvider=ViewModelProviders.of(this)
        //val quizViewModel=ViewModelProvider(this).get(QuizViewModel::class.java)
        //Log.d(TAG,"got a quizViewModel: $quizViewModel")

        trueButton=findViewById(R.id.true_button)//R non esiste piu, ma la notazione rimane
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        questionTextView=findViewById(R.id.question_text_view)
        previousButton=findViewById(R.id.previous_button)
        cheatButton=findViewById(R.id.cheat_button)

        trueButton.setOnClickListener{
            checkAnswer(true)
        }
        falseButton.setOnClickListener{
            checkAnswer(false)
        }
        nextButton.setOnClickListener{
            nextQuestion()
        }
        questionTextView.setOnClickListener{
            nextQuestion()
        }
        previousButton.setOnClickListener {
            prevQuestion()
        }

        cheatButton.setOnClickListener { view->
            val answerIsTrue=quizViewModel.currentQuestionAnswer
            //val intent = Intent(this,CheatActivity::class.java)
            val intent = CheatActivity.newIntent(this,answerIsTrue)
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {//M stands for marshmallow
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }//this code is unnecessary, but just for example
        }
        updateQuestion()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.currentQuestionCheated= data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false)?: false
            if (quizViewModel.currentQuestionCheated){
                cheatCounter+=1
                if (cheatCounter==3){
                    cheatButton.isEnabled=false
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Log.d(TAG,"onSaveInstanceState")
        outState.putInt(KEY_INDEX,quizViewModel.currentIndex)//salva solo la posizione, ma non salva le domande risposte
        outState.putInt(KEY_CHEAT_COUNTER,cheatCounter)
        //outState.put(KEY_ALREADY_REPLIED,quizViewModel.questionBank.alreadyReplied)
    }

    private fun nextQuestion(){
        quizViewModel.moveToNext()
        updateQuestion()
    }
    private fun prevQuestion(){
        quizViewModel.moveToPrevious()
        updateQuestion()
    }
    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer=quizViewModel.currentQuestionAnswer
        val toastMessage:Int
        when{
            quizViewModel.currentQuestionCheated -> {
                toastMessage = R.string.judgment_toast
            }
            userAnswer==correctAnswer -> {
                toastMessage=R.string.correct_toast
                percentageScore += 1f / quizViewModel.questionBankSize * 100f
            }
            else -> toastMessage=R.string.incorrect_toast
        }
        quizViewModel.currentQuestionAlreadyReplied=true
        repliedCounter++
        if (repliedCounter!=quizViewModel.questionBankSize){
            Toast.makeText(this,toastMessage,Toast.LENGTH_SHORT).show()
        }else{
            val finalScore=percentageScore.roundToInt()
            Toast.makeText(this,"Your percentage score is: $finalScore%",Toast.LENGTH_LONG).show()
        }
        checkReplied()
    }

    private fun updateQuestion(){
        questionTextView.setText(quizViewModel.currentQuestionText)
        checkReplied()
    }

    private fun checkReplied(){
        trueButton.isEnabled = !quizViewModel.currentQuestionAlreadyReplied
        falseButton.isEnabled = !quizViewModel.currentQuestionAlreadyReplied
    }

    /*override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }*/
}