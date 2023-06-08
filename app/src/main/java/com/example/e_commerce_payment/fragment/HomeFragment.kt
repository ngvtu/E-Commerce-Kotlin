package com.example.e_commerce_payment.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.adapter.ProductsAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentHomeBinding
import com.example.e_commerce_payment.models.ProductItems
import com.example.e_commerce_payment.models.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HomeFragment : Fragment(), ProductsAdapter.OnProductDeleteListener {

    private lateinit var listItemsSale: List<ProductItems>
    private lateinit var listItemsNew: List<ProductItems>
    private lateinit var mListItems: List<ProductItems>
    private lateinit var context: Context
    private lateinit var rcvListItemSale: RecyclerView
    private lateinit var rcvListItemNew: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fragmentContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        fragmentContext = requireContext()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchProductsSale()
        fetchProductsNew()

    }

    private fun fetchProductsNew() {
        listItemsNew = ArrayList()
        rcvListItemNew = binding.rcvListItemNew
        rcvListItemNew.setHasFixedSize(true)
        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rcvListItemNew.layoutManager = linearLayoutManager


        val retrofit: Retrofit = ApiConfig.setUpRetrofit()
        val apiService: ApiService = retrofit.create(ApiService::class.java)

        val call = apiService.getProducts()
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let {
                        val productList = it.products
                        productsAdapter = ProductsAdapter(productList, fragmentContext, this@HomeFragment )

                        // Set the adapter to the RecyclerView
                        rcvListItemNew.adapter = productsAdapter

                        Log.d("HomeFragment", "Call api done! ")
                    }
                } else {
                    // Handle API call failure
                    Log.e("HomeFragment", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                // Handle network or API call failure
                Log.e("HomeFragment", "Call Api fail is: ${t.message}")
            }
        })

    }

    private fun fetchProductsSale() {
        listItemsSale = ArrayList()

        rcvListItemSale = binding.rcvListItemSale
        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rcvListItemSale.layoutManager = linearLayoutManager


        val retrofit: Retrofit = ApiConfig.setUpRetrofit()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProducts()
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let {
                        val productList = it.products
                        productsAdapter = ProductsAdapter(productList, fragmentContext, this@HomeFragment  )

                        // Set the adapter to the RecyclerView
                        rcvListItemSale.adapter = productsAdapter

//                        Log.d("HomeFragment", "onResponse: ${productList[1].productName}")
                    }
                } else {
                    // Handle API call failure
                    Log.e("HomeFragment", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                // Handle network or API call failure
                Log.e("HomeFragment", "Call Api fail is: ${t.message}")
            }
        })
    }

    override fun onProductDelete(productId: Int) {

    }
}