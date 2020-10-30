package com.example.flightdroid

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object FirebaseUtils : Application(){
    var auth: FirebaseAuth? = Firebase.auth
    var database: DatabaseReference = Firebase.database.reference
//    val database = Firebase.database
//    val myRef = database.getReference("message")
}