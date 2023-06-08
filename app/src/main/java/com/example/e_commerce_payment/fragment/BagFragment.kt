package com.example.e_commerce_payment.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.activity.CheckoutActivity
import com.example.e_commerce_payment.adapter.ProductsInCartAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentBagBinding
import com.example.e_commerce_payment.models.MessagesResponse
import com.example.e_commerce_payment.models.ProductsInCartItems
import com.example.e_commerce_payment.models.ProductsInCartResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BagFragment : Fragment(), ProductsInCartAdapter.OnProductDeleteListener {

    private lateinit var binding: FragmentBagBinding
    private lateinit var listProductsInCartItems: ArrayList<ProductsInCartItems>
    private lateinit var productsInCartAdapter: ProductsInCartAdapter
    private lateinit var rcvListProductsItemInCart: RecyclerView
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(requireContext())
    }
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBagBinding.inflate(inflater, container, false)
        rcvListProductsItemInCart = binding.rcvListProductsItemInCart
        rcvListProductsItemInCart.setHasFixedSize(true)
        rcvListProductsItemInCart.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        token = "Bearer " + myPreferenceManager.getToken()

        fetchProductsInCart()
        addEvents()
        return binding.root
    }

    private fun addEvents() {
        binding.btnCheckOut.setOnClickListener {
           val intent = Intent(activity, CheckoutActivity::class.java)
            val bundle = Bundle()
            bundle.putFloat("totalAmount", binding.tvTotalAmount.text.toString().toFloat())

            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


    private fun fetchProductsInCart() {
        listProductsInCartItems = ArrayList()

        val retrofit: Retrofit = ApiConfig.setUpRetrofit()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProductsInCart(token)
        call.enqueue(object : Callback<ProductsInCartResponse> {
            override fun onResponse(
                call: Call<ProductsInCartResponse>,
                response: Response<ProductsInCartResponse>
            ) {
                if (response.isSuccessful) {
                    val productsInCartResponse: ProductsInCartResponse? = response.body()
                    val productsInCart = productsInCartResponse?.data
                    if (productsInCart != null) {
                        Log.d(
                            "BagFragment",
                            "Success to get products in cart!  ${productsInCart.size}"
                        )
                        for (productInCart in productsInCart) {
                            listProductsInCartItems.add(productInCart)
                            productsInCartAdapter = ProductsInCartAdapter(
                                listProductsInCartItems,
                                context,
                                this@BagFragment
                            )
                            binding.tvTotalAmount.text = calculateTotalAmount().toString()

                            productsInCartAdapter.setOnQuantityChangeListener(object : ProductsInCartAdapter.OnQuantityChangeListener {
                                override fun onQuantityChanged() {
                                    // Tính toán lại tổng số tiền
                                    val totalAmount = calculateTotalAmount()

                                    // Cập nhật lại giá trị của tvTotalAmount
                                    binding.tvTotalAmount.text = totalAmount.toString()
                                }
                            })

                            rcvListProductsItemInCart.adapter = productsInCartAdapter
                        }
                    }
                } else {
                    Log.e("BagFragment", "Failed to get products in cart")
                }
            }

            override fun onFailure(call: Call<ProductsInCartResponse>, t: Throwable) {
                Log.e("BagFragment", "Failed to get products in cart: ${t.message}")
            }
        })
    }

    private fun updateListAfterProductDelete(productId: Int) {
        val updatedList = listProductsInCartItems.filter { it.products.id != productId }
        productsInCartAdapter.setData(updatedList)
    }

    private fun deleteProductFromCart(deleteProductInCart: Int) {
        val retrofit: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)

        val token: String = "Bearer " + myPreferenceManager.getToken()
        val call = retrofit.deleteProductInCart(token, deleteProductInCart)
        call.enqueue(object : Callback<MessagesResponse> {
            override fun onResponse(
                call: Call<MessagesResponse>,
                response: Response<MessagesResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(
                        "BagFragment",
                        "Success to delete product in cart ${response.body()?.message}"
                    )
                    fetchProductsInCart()

                } else {
                    Log.e("BagFragment", "Failed to delete product in cart")
                }
            }

            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                Log.e("BagFragment", "Failed to delete product in cart: ${t.message}")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onProductDelete(productId: Int) {
        // Gọi API xóa sản phẩm khỏi giỏ hàng
        deleteProductFromCart(productId)

        // Cập nhật lại danh sách
        updateListAfterProductDelete(productId)

        calculateTotalAmount()
    }

    private fun calculateTotalAmount(): Double {
        var totalAmount = 0.0
        for (productInCart in listProductsInCartItems) {
            val priceTotalItems =
                (productInCart.products.sellingPrice - productInCart.products.discountPrice) * productInCart.quantity
            totalAmount += priceTotalItems
        }
        return totalAmount
    }


}