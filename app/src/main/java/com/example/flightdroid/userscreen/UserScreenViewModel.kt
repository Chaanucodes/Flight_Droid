package com.example.flightdroid.userscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightdroid.FirebaseUtils.auth
import com.example.flightdroid.FirebaseUtils.database
import com.example.flightdroid.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class UserScreenViewModel : ViewModel() {

    private var viewModelJob  = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val ref = database.child("users").child(auth?.uid!!)
    private var _userInfo = MutableLiveData<User?>()
    val userInfo : LiveData<User?>
    get() = _userInfo

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get Post object and use the values to update the UI
                        _userInfo.value = dataSnapshot.getValue<User?>()
                        Log.i("TAG_USERSCREENMODEL", "Valoos : ${_userInfo.value!!.email} ${_userInfo.value!!.number}")

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("TAG_USERSCREENMODEL", "loadPost:onCancelled", databaseError.toException())
                        // ...
                    }
                }
                ref.addValueEventListener(postListener)
            }
        }

    }
}