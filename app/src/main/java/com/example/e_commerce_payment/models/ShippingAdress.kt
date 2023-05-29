package com.example.e_commerce_payment.models

data class ShippingAdress(
    val address: String,
    val district: String,
    val fullName: String,
    val phone: String,
    val province: String,
    val ward: String
)