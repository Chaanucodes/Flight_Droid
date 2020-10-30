package com.example.flightdroid.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightdroid.FirebaseUtils
import com.example.flightdroid.FirebaseUtils.auth
import com.example.flightdroid.FirebaseUtils.database
import com.example.flightdroid.user.User
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : ViewModel() {

    private var _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private var _showError : Boolean
    val showError: Boolean
        get() = _showError

    private var _emailValid : Boolean
    val emailValid: Boolean
        get() = _emailValid

    var errorF = ""

    init {
        _emailValid = false
        _showError = false
    }


    fun createAccount(
        email: String,
        password: String,
        fName: String,
        lastName: String,
        number: String
    ) {
        FirebaseUtils.auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG_LOG", "createUserWithEmail:success")

                    _showError = false
                    writeNewUser(fName, lastName, email, number)
                    _showProgress.value = false



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG_LOG", "createUserWithEmail:failure : ${task.exception}")
                    errorF = task.exception.toString()
                    _showError = true
                    _showProgress.value = false

                }
            }
    }

    private fun writeNewUser(fName: String, lName: String, email : String, number : String) {
        val user = User(fName, lName, email, number)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                database.child("users").child(auth!!.uid!!).setValue(user)
            }
        }

    }


    fun validateEmail(email: String){
        _emailValid =  email.validEmail()
    }
}