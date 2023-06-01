package com.example.e_commerce_payment.authenticate

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.e_commerce_payment.CIPHERTEXT_WRAPPER
import com.example.e_commerce_payment.SHARED_PREFS_FILENAME
import com.example.e_commerce_payment.activity.ForgotPasswordActivity
import com.example.e_commerce_payment.databinding.ActivityLoginAgainBinding
import com.example.e_commerce_payment.storage.MyPreferenceManager
import java.util.concurrent.Executor

class LoginAgainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginAgainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@LoginAgainActivity)
    }

    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAgainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkDeviceHasBiometric()


        addEvents()
    }


    private fun checkDeviceHasBiometric() {
        val biometricManager = androidx.biometric.BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS ->
                Toast.makeText(this@LoginAgainActivity, "You can use the fingerprint sensor to login", Toast.LENGTH_SHORT).show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(this@LoginAgainActivity, "The device don't have fingerprint sensor", Toast.LENGTH_SHORT).show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this@LoginAgainActivity, "The biometric sensor is currently unavailable", Toast.LENGTH_SHORT).show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(this@LoginAgainActivity, "Your device don't have any fingerprint saved, please check your security settings", Toast.LENGTH_SHORT).show()
        }


        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnGotoForgot.setOnClickListener {
            intent.setClass(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignInBiometric.setOnClickListener{
            Toast.makeText(this@LoginAgainActivity, "Biometric", Toast.LENGTH_SHORT).show()

            biometricPrompt.authenticate(promptInfo)

        }

        binding.btnLogin.setOnClickListener {
            Toast.makeText(this@LoginAgainActivity, "Login", Toast.LENGTH_SHORT).show()
        }

        binding.btnLoginFb.setOnClickListener {
            Toast.makeText(this@LoginAgainActivity, "Login Facebook", Toast.LENGTH_SHORT).show()
        }

        binding.btnLoginGg.setOnClickListener {
            Toast.makeText(this@LoginAgainActivity, "Login Google", Toast.LENGTH_SHORT).show()
        }

    }
}