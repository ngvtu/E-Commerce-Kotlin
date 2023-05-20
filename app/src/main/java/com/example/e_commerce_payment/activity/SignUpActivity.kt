package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.Validator
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.api.SignUpResponse
import com.example.e_commerce_payment.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity(), OnClickListener, Validator {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var mail: String
    private lateinit var pass: String
    private lateinit var passConfirm: String

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

        mail = binding.edtEmail.text.toString().trim()
        pass = binding.edtPassword.text.toString().trim()
        passConfirm = binding.edtConfirmPassword.text.toString().trim()

        if (validateEmail(mail) && validatePassword(pass)) {

            Log.d("SignUpActivity", "signUp: ")

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService: ApiService = retrofit.create(ApiService::class.java)

            val call = apiService.register(mail, pass, passConfirm)

            call.enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if (response.isSuccessful) {
                        Log.d("SignUpActivity", "onResponse: ${response.body()}")

                        val signUpResponse: SignUpResponse? = response.body()

                        if (signUpResponse != null) {
                            val message = signUpResponse.message

                            when (message) {
                                "Sign up successfully" -> {
                                    Log.d("SignUpActivity", "onResponse: $message")

                                    // tạo 1 dialog đăng ký thành công

                                    intent = intent.setClass(this@SignUpActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else -> {
                                    Log.d("SignUpActivity", "onResponse: Unknown message - $message")
                                }
                            }
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()

                        Log.e("SignUpActivity", "onResponse error: $errorBody")
                        Toast.makeText(
                            this@SignUpActivity,
                            "Tài khoản đã tồn tại",
                            Toast.LENGTH_SHORT
                        ).show()
                        // để dialog đã đăng ký tài khoản ởdđây
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Log.e("SignUpActivity", "onFailure: ${t.message}")
                }
            })

        } else {
            Log.e("SignUpActivity", "signUp: failed" )
        }
    }

    private fun backToExit() {
        onBackPressed()
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
        if (password.isNotEmpty() && password.length >= 6 && binding.edtPassword.text.toString()
                .trim() == binding.edtConfirmPassword.text.toString().trim()
        ) {
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
        } else if (binding.edtPassword.text.toString()
                .trim() == binding.edtConfirmPassword.text.toString().trim()) {
            binding.layoutPassword.error = "Password and confirm password must be the same"
            binding.edtPassword.requestFocus()
            return false
        } else {
            binding.layoutPassword.error = "Something went wrong"
            binding.edtPassword.requestFocus()
            return false
        }
    }
}