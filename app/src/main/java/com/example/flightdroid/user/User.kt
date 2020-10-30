package com.example.flightdroid.user

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var firstName : String? = "A",
    var lastName : String? = "A",
    var email: String? = "A",
    var number: String? = "A"
)