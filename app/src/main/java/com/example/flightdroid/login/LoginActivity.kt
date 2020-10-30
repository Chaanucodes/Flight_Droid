package com.example.flightdroid.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightdroid.R
import com.example.flightdroid.databinding.ActivityLoginBinding
import com.example.flightdroid.userscreen.UserScreenActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val pass = binding.editTextLoginPass.text.toString()

            if(pass.isNotBlank() && email.isNotBlank()){
                viewModel.login(email, pass)
                binding.progressBar.visibility = View.VISIBLE
            }else Toast.makeText(this, "Please fill all the values", Toast.LENGTH_LONG).show()
        }

        viewModel.showProgress.observe(this, Observer {
            if(!it) {
                binding.progressBar.visibility = View.GONE
                if(!viewModel.showError) Toast.makeText(this, "SUCCESS!", Toast.LENGTH_LONG).show()
                Intent(this, UserScreenActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
            if(viewModel.showError) Toast.makeText(this, viewModel.errorF, Toast.LENGTH_LONG).show()
        })
    }
}