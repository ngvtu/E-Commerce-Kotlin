package com.example.e_commerce_payment.api

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.databinding.ActivityTestApiResponseBinding
import com.example.e_commerce_payment.models.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestApiResponse : AppCompatActivity() {
    private val apiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
    private lateinit var binding: ActivityTestApiResponseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestApiResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {

        binding.button.setOnClickListener {
            Log.d("TestApiResponse", "addEvents: ")

            apiService.getProducts().enqueue(object : Callback<ProductResponse> {
                override fun onResponse(
                    call: Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {
                    if (response.isSuccessful) {
                        val products = response.body()?.products
                        Log.d("TestApiResponse", "onResponse: $products")


                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    Log.d("TestApiResponse", "onFailure: ${t.message}")
                }
            })
        }

    }
}