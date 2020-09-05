package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

private const val TAG = "CheatActivity"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true"
private const val KEY_CHEAT = "cheat"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView : TextView
    private lateinit var showAnswerButton : Button
    private var answerIsTrue = false


    //stash the QuizViewModel instance associated with the activity
    //'by lazy' allows to use immutable val instead of var
    private val cheatViewModel : CheatingViewModel by lazy{
        ViewModelProvider(this).get(CheatingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val isAnswerShown = savedInstanceState?.getBoolean(KEY_CHEAT,
            false) ?: false
        cheatViewModel.setCheater(isAnswerShown)
        
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        if(cheatViewModel.getCheater()) {//keep as cheater upon rotation
            showAnswerButtonClicked()
            showAnswerButton.isEnabled = false //disable
        }

        showAnswerButton.setOnClickListener{
            showAnswerButtonClicked()
            showAnswerButton.isEnabled = false //disable
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        //save current index to not loose track when stopped
        savedInstanceState.putBoolean(KEY_CHEAT, cheatViewModel.getCheater())
    }

    private fun setAnswerShownResult(isAnswerShown : Boolean){
        //sending an extra within the intent back to the parent activity
        cheatViewModel.setCheater(true)
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)//set result of action in intent?
    }

    private fun showAnswerButtonClicked(){
        val answerText = when{
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        //set answer ID
        answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    //static function, no need for class instance (companion)
    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(
                packageContext,
                CheatActivity::class.java
                ).apply {
                    putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                }
        }
    }
}