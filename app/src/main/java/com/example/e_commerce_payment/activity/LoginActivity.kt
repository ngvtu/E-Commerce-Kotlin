package com.example.e_commerce_payment.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var mail: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener(this)
        binding.btnLoginFb.setOnClickListener(this)
        binding.btnGotoForgot.setOnClickListener(this)
        binding.btnLoginGg.setOnClickListener(this)
        binding.btnGotoSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        var loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        var loginPrefsEditor = loginPreferences.edit()
        var saveLogin = loginPreferences.getBoolean("saveLogin", false)
        if (saveLogin == true) {
            binding.edtEmail.setText(loginPreferences.getString("username", ""))
            binding.edtPassword.setText(loginPreferences.getString("password", ""))
            binding.cbxRemember.isChecked = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnBack.id -> {
                backToExit()
            }

            binding.btnLoginFb.id -> {
                loginFb()
            }

            binding.btnLoginGg.id -> {
                loginGg()
            }

            binding.btnGotoForgot.id -> {
                gotoForgotPassword()
            }

            binding.btnLogin.id -> {
                login()
            }

            binding.btnGotoSignUp.id -> {
                gotoSignUp()
            }
        }
    }

    private fun gotoSignUp() {
        intent = intent.setClass(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun loginFb() {
        Toast.makeText(this, "Login with Fb coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun loginGg() {
        Toast.makeText(this, "Login with Gg coming soon", Toast.LENGTH_SHORT).show()

    }

    private fun gotoForgotPassword() {
        intent = intent.setClass(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
//        finish()
    }

    private fun login() {

        val mail = binding.edtEmail.text.toString().trim()
        val pass = binding.edtPassword.text.toString().trim()

        if (validateEmail(mail) && validatePassword(pass)){
            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun backToExit() {
//        onBackPressed()
        intent = intent.setClass(this, AppIntro::class.java)
        startActivity(intent)
//        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.error = ""
            binding.layoutEmail.setEndIconDrawable(R.drawable.ic_tick)
            true
        } else if (email.isEmpty()) {
            binding.layoutEmail.error = "Email can't be empty"
            binding.edtEmail.requestFocus()
            false
        } else if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.error = "Not a valid email address. Should be your@email.com"
            binding.edtEmail.requestFocus()
            false
        } else {
            binding.layoutEmail.error = "Something went wrong"
            binding.edtEmail.requestFocus()
            false
        }
    }

    private fun validatePassword(password: String): Boolean{
        if (password.isNotEmpty() && password.length >= 6) {
            binding.layoutPassword.error = ""
            binding.layoutPassword.setEndIconDrawable(R.drawable.ic_tick)
            return true
        } else if (password.isEmpty()) {
            binding.layoutPassword.error = "Password can't be empty"
            binding.edtPassword.requestFocus()
            return false
        } else if (password.isNotEmpty() && password.length < 6) {
            binding.layoutPassword.error = "Password must be at least 6 characters"
            binding.edtPassword.requestFocus()
            return false
        } else {
            binding.layoutPassword.error = "Something went wrong"
            binding.edtPassword.requestFocus()
            return false
        }
    }


}