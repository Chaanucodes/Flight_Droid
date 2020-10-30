package com.example.flightdroid.userscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightdroid.FirebaseUtils.auth
import com.example.flightdroid.R
import com.example.flightdroid.databinding.ActivityUserScreenBinding
import com.example.flightdroid.login.LoginActivity

class UserScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_screen)
        binding.lifecycleOwner = this
        val viewModel = ViewModelProviders.of(this).get(UserScreenViewModel::class.java)

        viewModel.userInfo.observe(this, Observer {
            it?.let {
                binding.textViewDFirstName.text = "First name: ${it.firstName}"
                binding.textViewDLastName.text = "Last name: ${it.lastName}"
                binding.textViewDEmail.text = "Email: ${it.email}"
                binding.textViewDNumber.text = "Number: ${it.number}"
            }
        })

        binding.buttonLogOut.setOnClickListener {
            auth?.signOut()
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
                finish()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = "Dashboard"
    }
}