package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener(this)
        binding.btnGotoLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.btnLoginFb.setOnClickListener(this)
        binding.btnLoginGg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnSignUp.id -> {
                signUp()
            }
            binding.btnBack.id -> {
                backToExit()
            }
            binding.btnGotoLogin.id -> {
                gotoLogin()
            }
            binding.btnLoginFb.id -> {
                loginFb()
            }
            binding.btnLoginGg.id -> {
                loginGg()
            }
        }
    }

    private fun loginGg() {
        Toast.makeText(this, "Login with gg coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun loginFb() {
        Toast.makeText(this, "Login with fb coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun gotoLogin() {
        intent = intent.setClass(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp() {
        Toast.makeText(this, "SignUp", Toast.LENGTH_SHORT).show()
    }

    private fun backToExit() {
//        intent = intent.setClass(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish()
        onBackPressed()
    }
}