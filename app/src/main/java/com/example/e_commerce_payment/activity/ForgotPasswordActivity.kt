package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()

    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener { this}
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnBack.id -> {
                onBackPressed()
                finish()
            }
        }
    }
}