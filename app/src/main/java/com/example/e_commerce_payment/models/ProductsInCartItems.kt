package com.example.e_commerce_payment.models

data class ProductsInCartItems(
    val created_at: String,
    val id: Int,
    val productColor: String,
    val productId: Int,
    val productSize: String,
    val products: Products,
    var quantity: Int,
    val subTotal: Int,
    val updated_at: String,
    val userId: Int
)