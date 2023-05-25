package com.example.e_commerce_payment.models

data class ProductResponse(

//    @SerializedName("data")
    val products: List<ProductItems>,
    val total: Int
)