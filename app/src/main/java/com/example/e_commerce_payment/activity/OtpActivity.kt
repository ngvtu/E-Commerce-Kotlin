package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityOtpBinding
import com.example.e_commerce_payment.models.OtpResponse
import com.example.e_commerce_payment.models.User
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var countDownTimer: CountDownTimer
    private val COUNTDOWN_TIME: Long = 2 * 60 * 1000 // 2 minutes in milliseconds
    private var timeLeftInMillis: Long = COUNTDOWN_TIME
    private var isTimerRunning = false
    private var doubleBackToExitPressedOnce: Boolean = false
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@OtpActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)




        startCountdownTimer()
        addEvents()
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            Toast.makeText(this@OtpActivity, "Backk", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        val email: String = intent.getStringExtra("email").toString()
        Log.d("OtpActivity", " email is ${email}")

        binding.btnVerification.setOnClickListener {
            val otpValue = binding.otpView.value
            Log.d("OtpActivity", "OTP input is: $otpValue")

            val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
            val call = apiService.verifyCodeOtp(email, otpValue)
            call.enqueue(object : Callback<OtpResponse> {
                override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                    if (response.isSuccessful) {
                        Log.d("OtpActivity", "Verify done!")
                        Toast.makeText(this@OtpActivity, "Success", Toast.LENGTH_SHORT).show()
                        val token: String = "Bearer " +response.body()!!.accessToken

                        myPreferenceManager.saveToken(response.body()!!.accessToken)

                        Log.i("OtpActivity", "This is token $token")
                        getProfile(token)

                        intent = intent.setClass(this@OtpActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        AestheticDialog.Builder(
                            this@OtpActivity,
                            DialogStyle.FLAT,
                            DialogType.ERROR
                        )
                            .setTitle("OTP incorrect!")
                            .setMessage("Check your mail again to input success")
                            .setDuration(Gravity.TOP)
                            .setCancelable(false)
                            .setDarkMode(false)
                            .setAnimation(DialogAnimation.SLIDE_DOWN)
                            .setOnClickListener(object : OnDialogClickListener {
                                override fun onClick(dialog: AestheticDialog.Builder) {
                                    dialog.dismiss()
                                    binding.otpView.value = ""
                                }
                            })
                            .show()
                    }
                }

                override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                    Log.e("OtpActivity", "Call api fail!")
                }

            })
        }
    }

    fun getProfile(token: String) {

        val apiService = ApiConfig.setUpRetrofit().create(ApiService::class.java).getProfile(token)
        apiService.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("ProfileFragment", "get profile done!@ ${response.body()}")
                    val user = response.body()

                    myPreferenceManager.saveProfile(
                        this@OtpActivity,
                        user?.id.toString(),
                        user?.email.toString(),
                        user?.fullName.toString(),
                        user?.phone.toString(),
                        user?.address.toString(),
                        user?.dateOfBirth.toString(),
                        user?.gender.toString()
                    )
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("ProfileFragment", "get profile failed!@ ${t.message}")

            }
        })
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
            .setOnClickListener(object : OnDialogClickListener {
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
}