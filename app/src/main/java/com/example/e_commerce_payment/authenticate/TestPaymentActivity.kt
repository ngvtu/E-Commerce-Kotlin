package com.example.e_commerce_payment.authenticate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityTestPaymentBinding
import com.example.e_commerce_payment.models.PaymentReponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestPaymentBinding
    private lateinit var btnPayment: Button
    private lateinit var edtPrice: EditText
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@TestPaymentActivity)
    }

    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    lateinit var paymentIntentClientSecret: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnPayment = binding.btnPayment
        edtPrice = binding.edtPrice

        val price = edtPrice.text.toString()

        payment(price)
        btnPayment.setOnClickListener {

            presentPaymentSheet()
        }
    }

    private fun payment(price: String) {
        paymentSheet  = PaymentSheet(this@TestPaymentActivity, ::onPaymentSheetResult)
        val token = "Bearer " +myPreferenceManager.getToken()
        val retrofit = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = retrofit.payment(token, price.toInt())
        call.enqueue(object : Callback<PaymentReponse> {
            override fun onResponse(
                call: Call<PaymentReponse>,
                response: Response<PaymentReponse>
            ) {
                if (response.isSuccessful){
                    val paymentResponse = response.body()
                    Log.d("TestPaymentActivity", paymentResponse.toString())
                    paymentIntentClientSecret = paymentResponse!!.paymentIntent
                    customerConfig = PaymentSheet.CustomerConfiguration(
                        paymentResponse.customer,
                        paymentResponse.ephemeralKey
                    )
                    val publishableKey = paymentResponse.publishableKey
                    PaymentConfiguration.init(this@TestPaymentActivity, publishableKey)

                    Toast.makeText(this@TestPaymentActivity, "Call success", Toast.LENGTH_SHORT).show()

                } else{
                    Log.d("TestPaymentActivity", "vao day")
                }
            }

            override fun onFailure(call: Call<PaymentReponse>, t: Throwable) {
                Log.d("TestPaymentActivity", "On Fail: ${t.message}")
            }

        })
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.d("TestPaymentActivity", "Canceled")
                Toast.makeText(this@TestPaymentActivity, "Canceled", Toast.LENGTH_SHORT).show()

            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this@TestPaymentActivity, "Error: ${paymentSheetResult.error}", Toast.LENGTH_SHORT).show()
                Log.d("TestPaymentActivity", "Error: ${paymentSheetResult.error}")

            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                Toast.makeText(this@TestPaymentActivity, "Suceessssssssss", Toast.LENGTH_SHORT).show()

            }
        }
    }
}