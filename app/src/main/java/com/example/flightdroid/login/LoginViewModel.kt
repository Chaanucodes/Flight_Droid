package com.example.flightdroid.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightdroid.FirebaseUtils.auth

class LoginViewModel : ViewModel() {

    private var _showError : Boolean
    val showError: Boolean
        get() = _showError

    private var _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    var errorF = ""

    init {
        _showError = false
    }
    fun login(email : String, password : String){
        _showProgress.value = true
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d("LOGIN_VIEWMODEL", "signInWithEmail:success")
                    _showError = false
                    _showProgress.value = false
                } else {
                    Log.w("LOGIN_VIEWMODEL", "signInWithEmail:failure", task.exception)
                    errorF = task.exception.toString()
                    _showError = true
                    _showProgress.value = false
                }
            }
    }
}