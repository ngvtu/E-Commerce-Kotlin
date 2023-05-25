package com.example.e_commerce_payment.models

data class ProductsInCartResponse(
    val `data`: List<ProductsInCartItems>,
    val message: String
)