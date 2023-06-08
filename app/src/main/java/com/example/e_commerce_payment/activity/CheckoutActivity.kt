package com.example.e_commerce_payment.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.databinding.ActivityCheckoutBinding
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
//
//    private var clientId =
//        "AU79549_7IMLkjoKHjvBXPoplust22ifGN2Gd963D2bcy6NyN70sVKRgsz1DcACZzOjyVxYQJq6asGg0"
 private var clientId =
        "ARlUHaIMQG5x1buWvuKQjhPt8lmrvgTkrpM0I5ROlFQRUWuXwFLgvQGRuH7aUbJtAAedM9ease4uvTRO"

    val PAYMENT_REQUEST_CODE = 123

    private lateinit var configuration: PayPalConfiguration

//    private var tvChangeAddress: TextView = findViewById(R.id.tvChange)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setAddress()
        setPayment()
        payment()
    }

    private fun payment() {

        configuration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientId)

        binding.btnSubmitOder.setOnClickListener {
            val amounts = binding.tvPriceSummary.text.toString()
            val payment = PayPalPayment(
                BigDecimal.valueOf(amounts.toDouble()),
                "USD",
                "Payment for your oder: ",
                PayPalPayment.PAYMENT_INTENT_SALE
            )

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
            startActivityForResult(intent, PAYMENT_REQUEST_CODE)
        }
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras
        val totalAmount = bundle?.getFloat("totalAmount")
        binding.tvPriceOder.text = totalAmount.toString()

        binding.tvPriceDelivery.text = 10.toString()
        if (totalAmount != null) {
            binding.tvPriceSummary.text = (totalAmount + 10).toString()
        }

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioCard -> {
                    binding.radioCard.setChecked(true)
                    binding.layoutPayment.setVisibility(View.VISIBLE)
                }

                R.id.radioCash -> binding.layoutPayment.setVisibility(View.GONE)
            }
        })

//        tvChangeAddress.setOnClickListener {
//            val intent = Intent(this@CheckoutActivity, ShippingAddressActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun setPayment() {

    }

    private fun setAddress() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val paymentConfirmation: PaymentConfirmation? =
                    data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (paymentConfirmation != null) {
                    try {
                        val paymentDetails: String = paymentConfirmation.toJSONObject().toString()
                        val objectt = JSONObject(paymentDetails)
                        Toast.makeText(this, objectt.getString("id"), Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show()
        }
    }

}