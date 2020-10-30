package com.example.flightdroid.signup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightdroid.FirebaseUtils.auth
import com.example.flightdroid.R
import com.example.flightdroid.WorkUtil
import com.example.flightdroid.databinding.ActivitySignUpBinding
import com.example.flightdroid.login.LoginActivity
import com.example.flightdroid.notifs.CHANNEL_ID
import com.example.flightdroid.user.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        val viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        // Sign UP button listener
        binding.buttonSignUp.setOnClickListener {
            val details = listOf(
                binding.editTextTextEmailAddress.text.toString(),
                binding.editTextTextPassword.text.toString(),
                binding.editTextTextFirstName.text.toString(),
                binding.editTextTextLastName.text.toString(),
                binding.editTextNumber.text.toString()
            )

                //Checking if fields are empty
            var blank = false
            details.forEach {
                if (it.isBlank()) {
                    blank = true
                }
            }

            //Passing values to ViewModel to send to Firebase/ Displaying error for not entering right details
            if (!blank && viewModel.emailValid) {
                viewModel.createAccount(details[0], details[1], details[2], details[3], details[4])
                binding.indeterminateBar.visibility = View.VISIBLE
            } else Toast.makeText(this, "Please fill all the details correctly", Toast.LENGTH_LONG)
                .show()

        }

        //Checking if email address format is correct
        binding.editTextTextEmailAddress.doOnTextChanged { text, start, count, after ->
            val email = text.toString().trim()
            if (email.isNotEmpty()) {
                viewModel.validateEmail(email)
                if (viewModel.emailValid) {
                    binding.textViewValidation.setTextColor(resources.getColor(R.color.validationTrue))
                    binding.textViewValidation.setText("!")
                } else {
                    binding.textViewValidation.setTextColor(resources.getColor(R.color.validatonFalse))
                    binding.textViewValidation.setText("!")
                }
            }
        }

        //Setting progress bar visibility and navigation to LoginActivity if auth is successful
        viewModel.showProgress.observe(this, Observer {
            if (!it) {
                binding.indeterminateBar.visibility = View.GONE
                if (!viewModel.showError) {
                    Toast.makeText(this, "SUCCESS!", Toast.LENGTH_LONG).show()
                    Intent(this, LoginActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
            if (viewModel.showError) Toast.makeText(this, viewModel.errorF, Toast.LENGTH_LONG).show()
        })

        //button to direct to login page
        binding.buttonExisting.setOnClickListener {
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        //Initiating recurring notifications using work manager
        WorkUtil.launchWork()
        createNotiChannel()
    }

    // Notification Channels setup
    private fun createNotiChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = applicationContext.resources.getString(R.string.channel_name)
            val descriptionText =
                applicationContext.resources.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}