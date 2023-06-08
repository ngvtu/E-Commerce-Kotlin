package com.example.e_commerce_payment.models

data class PaymentReponse(
    val customer: String,
    val ephemeralKey: String,
    val paymentIntent: String,
    val publishableKey: String
)