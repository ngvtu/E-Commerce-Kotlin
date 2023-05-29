package com.example.e_commerce_payment.models

data class AddressItems(
    val address: String,
    val district: String,
    val fullName: String,
    val id: Int,
    val phone: String,
    val postcode: String,
    val province: String,
    val userId: String,
    val ward: String
)