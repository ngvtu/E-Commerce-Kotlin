package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.databinding.ActivityOtpBinding
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var countDownTimer: CountDownTimer
    private val COUNTDOWN_TIME: Long = 3 * 60 * 1000 // 3 minutes in milliseconds
    private var timeLeftInMillis: Long = COUNTDOWN_TIME
    private var isTimerRunning = false
    private var doubleBackToExitPressedOnce: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCountdownTimer()
        addEvents()
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (timeLeftInMillis / 1000) / 60
                val seconds = (timeLeftInMillis / 1000) % 60
                val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                binding.tvTimeCountdown.text = timeFormatted
            }

            override fun onFinish() {
                binding.tvTimeCountdown.text = "Request time out!"
                isTimerRunning = false
                Toast.makeText(this@OtpActivity, "Finish", Toast.LENGTH_SHORT).show()
            }
        }
        countDownTimer.start()
        isTimerRunning = true

    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            Toast.makeText(this@OtpActivity, "Backk", Toast.LENGTH_SHORT).show()
        }

        binding.btnVerification.setOnClickListener {
            val otpValue = binding.otpView.value
            if (otpValue == "aaaaaa"){
                Toast.makeText(this@OtpActivity, "Success", Toast.LENGTH_SHORT).show()

                intent = intent.setClass(this@OtpActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                AestheticDialog.Builder(this@OtpActivity, DialogStyle.FLAT, DialogType.ERROR)
                    .setTitle("OTP incorrect!")
                    .setMessage("Check your mail again to input success")
                    .setDuration(Gravity.TOP)
                    .setCancelable(false)
                    .setDarkMode(false)
                    .setAnimation(DialogAnimation.SLIDE_DOWN)
                    .setOnClickListener(object : OnDialogClickListener{
                        override fun onClick(dialog: AestheticDialog.Builder) {
                            dialog.dismiss()
                            binding.otpView.value = ""
                        }
                    })
                    .show()
            }
        }

        binding.tvResendOtp.setOnClickListener {
            Toast.makeText(this@OtpActivity, "Resend OTP", Toast.LENGTH_SHORT).show()
            resetTimer()
            binding.otpView.value = ""
            startCountdownTimer()
        }
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        AestheticDialog.Builder(this@OtpActivity, DialogStyle.TOASTER, DialogType.WARNING)
            .setTitle("Warning")
            .setMessage("If you EXIT this process you can't authenticate you account!")
            .setGravity(Gravity.TOP)
            .setAnimation(DialogAnimation.SLIDE_DOWN)
            .setCancelable(true)
            .setOnClickListener(object : OnDialogClickListener{
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    doubleBackToExitPressedOnce = false
                }
            })
            .show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}