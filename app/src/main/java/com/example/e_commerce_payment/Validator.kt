package com.example.e_commerce_payment

import android.util.Patterns

interface Validator {
    fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }
}