package com.example.e_commerce_payment.models

data class User(
    val address: Any,
    val created_at: String,
    val dateOfBirth: Any,
    val email: String,
    val fullName: String?,
    val gender: String,
    val id: Int,
    val image: String,
    val phone: Any,
    val refreshToken: String,
    val role: Int,
    val updated_at: String
)