package com.example.e_commerce_payment.activity.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.activity.LoginActivity
import com.example.e_commerce_payment.databinding.ActivitySettingsBinding
import com.example.e_commerce_payment.storage.MyPreferenceManager

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var myPreferenceManager: MyPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {
        myPreferenceManager = MyPreferenceManager(this@SettingsActivity)

        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.btnSignOut.setOnClickListener{
            myPreferenceManager.clearLoginData()
            intent = intent.setClass(this@SettingsActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}