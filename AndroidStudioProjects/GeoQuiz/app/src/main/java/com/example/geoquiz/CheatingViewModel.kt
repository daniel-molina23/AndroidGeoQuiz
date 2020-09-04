package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "CheatingViewModel"

class CheatingViewModel : ViewModel(){

    private var isCheater = false //innocent until proven guilty

    fun getCheater() : Boolean{
        return isCheater
    }

    fun setCheater(value: Boolean){
        isCheater = value
    }
}