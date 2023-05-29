package com.example.e_commerce_payment.activity.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
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

        binding.btnChangePass.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_change_password)




            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }


        binding.btnSignOut.setOnClickListener{
            myPreferenceManager.clearLoginData()
            intent = intent.setClass(this@SettingsActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}