package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.Validator
import com.example.e_commerce_payment.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity(), OnClickListener, Validator {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()

    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnBack.id -> {
                onBackPressed()
                finish()
            }

            binding.btnSend.id -> {
                sendEmailResetPassword()
            }
        }
    }

    private fun sendEmailResetPassword() {
        if (validateEmail(binding.edtEmail.text.toString())) {
            Toast.makeText(this, "Send email request done! ", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Send email request failed! ", Toast.LENGTH_SHORT).show()
        }
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

}