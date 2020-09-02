package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton: Button
    private lateinit var nextButton : Button
    private lateinit var prevButton : Button
    private lateinit var questionTextView : TextView
    private var scoreCount : Int = 0


    //stash the QuizViewModel instance associated with the activity
    //'by lazy' allows to use immutable val instead of var
    private val quizViewModel : QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener{
            checkAnswer(true)
            quizViewModel.markCurrentQuestionAnswered()

            quizViewModel.moveToNext()
            //must be used after index change
            checkButtonTFEnablement() //toggl on/off true false buttons
            updateQuestion()
            checkCompletenessAndScore()
        }

        falseButton.setOnClickListener{
            checkAnswer(false)
            quizViewModel.markCurrentQuestionAnswered()

            quizViewModel.moveToNext()
            //must be used after index change
            checkButtonTFEnablement() //toggl on/off true false buttons
            updateQuestion()
            checkCompletenessAndScore()
        }

        nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            //must be used after the index change
            checkButtonTFEnablement() //toggl on/off true false buttons
            updateQuestion()
        }

        prevButton.setOnClickListener{
            quizViewModel.moveToPrevious()
            //must be used after index change
            checkButtonTFEnablement() //toggl on/off true false buttons
            updateQuestion()
        }

//        questionTextView.setOnClickListener{
//            currentIndex = (currentIndex + 1) % questionBank.size
//            updateQuestion()
//        }
        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun checkButtonTFEnablement(){
        //must be used after the currentIndex change
        if(quizViewModel.currentQuestionAnswered){
            //next question WAS answered, DISABLE
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }else{
            //next question NOT answered, ENABLE
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkCompletenessAndScore(){
        var completedQuiz: Boolean = quizViewModel.boolFinishedQuiz()

        if(completedQuiz){
            //disable everything
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            nextButton.isEnabled = false
            prevButton.isEnabled = false

            //output final score
            var score = scoreCount.toFloat()/quizViewModel.questionSize() * 100
            var message = "Final Score: " + "%.2f ".format(score) + "%"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAnswer(userAnswer : Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if(userAnswer == correctAnswer){
            scoreCount += 1
            R.string.correct_toast
        } else{
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionResID
        questionTextView.setText(questionTextResId)
    }
}