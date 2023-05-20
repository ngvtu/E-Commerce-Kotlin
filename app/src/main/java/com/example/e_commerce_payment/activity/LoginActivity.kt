package com.example.e_commerce_payment.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.Validator
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.api.LoginResponse
import com.example.e_commerce_payment.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener, Validator {
    private lateinit var binding: ActivityLoginBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var loginPreferences: SharedPreferences
    private lateinit var loginPrefsEditor: SharedPreferences.Editor
    private lateinit var mail: String
    private lateinit var pass: String


    private var saveLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener(this)
        binding.btnLoginFb.setOnClickListener(this)
        binding.btnLoginGg.setOnClickListener(this)
        binding.btnGotoSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.btnGotoForgot.setOnClickListener(this)

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        loginPrefsEditor = loginPreferences.edit()
        saveLogin = loginPreferences.getBoolean("saveLogin", false)

        if (saveLogin) {
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
    }

    private fun login() {

        mail = binding.edtEmail.text.toString().trim()
        pass = binding.edtPassword.text.toString().trim()

        if (validateEmail(mail) && validatePassword(pass)) {


            val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
            val call = apiService.login(mail, pass)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse: LoginResponse? = response.body()
                        if (loginResponse != null){
                            val token = loginResponse.accessToken
                            Log.d("LoginActivity", "onResponse: $token")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginActivity", "onFailure: $errorBody")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity", "onFailure: ${t.message.toString()}")
                }
            })

        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backToExit() {
        intent = intent.setClass(this, AppIntro::class.java)
        startActivity(intent)
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

    override fun validateEmail(email: String): Boolean {
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

    override fun validatePassword(password: String): Boolean {
        if (password.isNotEmpty() && password.length >= 6) {
            binding.layoutPassword.error = ""
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