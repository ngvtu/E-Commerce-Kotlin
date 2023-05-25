package com.example.e_commerce_payment.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.adapter.ProductsInCartAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentBagBinding
import com.example.e_commerce_payment.models.ProductsInCartItems
import com.example.e_commerce_payment.models.ProductsInCartResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BagFragment : Fragment() {
    private lateinit var binding: FragmentBagBinding
    private lateinit var listProductsInCartItems: ArrayList<ProductsInCartItems>
    private lateinit var productsInCartAdapter: ProductsInCartAdapter
    private lateinit var rcvListProductsItemInCart: RecyclerView



    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBagBinding.inflate(inflater, container, false)
        rcvListProductsItemInCart = binding.rcvListProductsItemInCart
        rcvListProductsItemInCart.setHasFixedSize(true)
        rcvListProductsItemInCart.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        fetchProductsInCart()

        return binding.root
    }

    private fun fetchProductsInCart() {
        listProductsInCartItems = ArrayList()

        val retrofit: Retrofit = ApiConfig.setUpRetrofit()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
        val token: String = "Bearer " + myPreferenceManager.getToken()
        val call = apiService.getProductsInCart(token)
        call.enqueue(object : Callback<ProductsInCartResponse> {
            override fun onResponse(
                call: Call<ProductsInCartResponse>,
                response: Response<ProductsInCartResponse>
            ) {
                if(response.isSuccessful) {
                    val productsInCartResponse: ProductsInCartResponse? = response.body()
                    val productsInCart = productsInCartResponse?.data
                    if (productsInCart != null) {
                        Log.d("BagFragment", "Success to get products in cart!  ${productsInCart.size}")
                        for (productInCart in productsInCart) {
                            listProductsInCartItems.add(productInCart)
                            productsInCartAdapter = ProductsInCartAdapter(listProductsInCartItems, context)
                            rcvListProductsItemInCart.adapter = productsInCartAdapter
                        }

                    }
                } else{
                    Log.e("BagFragment", "Failed to get products in cart")
                }
            }

            override fun onFailure(call: Call<ProductsInCartResponse>, t: Throwable) {
                Log.e("BagFragment", "Failed to get products in cart: ${t.message}")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}