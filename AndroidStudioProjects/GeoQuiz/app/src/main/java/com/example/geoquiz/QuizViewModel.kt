package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){

    var currentIndex = 0 //made not private anymore so it can be accessed by external classes
    var isCheater = false

    //calling the Question constructor to make list of objects
    private val questionBank = listOf(
        Question(R.string.question_giza_pyramid, true),
        Question(R.string.question_gun_powder, true),
        Question(R.string.question_tomatoes, true),
        Question(R.string.question_china, false),
        Question(R.string.question_socrates, false))

    val currentQuestionAnswer : Boolean get() = questionBank[currentIndex].answer

    val currentQuestionResID : Int get() = questionBank[currentIndex].textResId

    val currentQuestionAnswered : Boolean get() = questionBank[currentIndex].questionAnswered

    fun markCorrect(){
        questionBank[currentIndex].correctAnswer = true
    }

    fun getScore() : Int{
        var count = 0
        for(question in questionBank){
            if(question.correctAnswer) count +=1
        }
        return count
    }

    fun markCurrentQuestionAnswered(){
        questionBank[currentIndex].questionAnswered = true
    }

    fun questionSize() : Int{
        return questionBank.size
    }

    fun boolFinishedQuiz() : Boolean{
        var completedQ = true
        for(question in questionBank){
            if(!question.questionAnswered){
                //if question not answered
                completedQ = false
                break
            }
        }
        return completedQ
    }

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
        isCheater = false //next one we restart
    }

    fun moveToPrevious(){
        currentIndex = if (currentIndex > 0){
            currentIndex - 1 //decrement
        } else{ // current index = 0
            questionBank.size - 1 // end of the list
        }
        isCheater = false //different question
    }
}