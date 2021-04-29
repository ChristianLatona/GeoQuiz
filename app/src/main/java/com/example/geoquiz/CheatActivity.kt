package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

//private const val TAG="CheatActivity"
private const val EXTRA_ANSWER_IS_TRUE="com.latonacristian.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN="com.latonacristian.android.geoquiz.answer_shown"
private const val KEY_IS_CHEATER="is_cheater"
class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView

    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProvider(this).get(CheatViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView=findViewById(R.id.answer_text_view)
        showAnswerButton=findViewById(R.id.show_answer_button)
        apiLevelTextView=findViewById(R.id.API_level_text_view)

        val version= Build.VERSION.SDK_INT
        apiLevelTextView.text = "API Level $version"

        cheatViewModel.cheated = false

        showAnswerButton.setOnClickListener {
            cheatViewModel.cheated = true
            val answerText = if (answerIsTrue) {
                R.string.true_button
            } else {
                R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult()
        }

        if (cheatViewModel.cheated){//previene il bug della rotazione e perdita dei dati
            val answerText = if (answerIsTrue) {
                R.string.true_button
            } else {
                R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult()
        }

        answerIsTrue=intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(KEY_IS_CHEATER,cheatViewModel.cheated)
    }

    private fun setAnswerShownResult(){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data) // set result its always present in explicit intent
    }

    companion object{
        fun newIntent(packageContext: Context, answerInTrue:Boolean): Intent {
            return Intent(packageContext,CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE,answerInTrue)
            }
        }
    }
}