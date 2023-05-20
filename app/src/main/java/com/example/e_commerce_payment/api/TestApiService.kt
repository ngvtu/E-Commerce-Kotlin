package com.example.e_commerce_payment.api

import retrofit2.http.GET

interface TestApiService {
    @GET("user")
    fun getInforUser(): String



}