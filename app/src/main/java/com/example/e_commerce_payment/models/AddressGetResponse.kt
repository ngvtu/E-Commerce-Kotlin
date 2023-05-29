package com.example.e_commerce_payment.models

data class AddressGetResponse(
    val `data`:  List<AddressItems>,
    val message: String
)