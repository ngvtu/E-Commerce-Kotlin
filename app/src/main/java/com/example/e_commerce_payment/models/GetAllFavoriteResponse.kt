package com.example.e_commerce_payment.models

data class GetAllFavoriteResponse(
    val `data`: List<ProductsInFavoriteItems>,
    val message: String
)