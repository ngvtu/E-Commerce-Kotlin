package com.example.e_commerce_payment.api

data class SignUpResponse(
    val data: UserData?,
    val message: String
)

data class UserData(
    val id: Int,
    val email: String
)