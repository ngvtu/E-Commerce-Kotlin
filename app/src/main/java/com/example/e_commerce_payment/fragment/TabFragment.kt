package com.example.e_commerce_payment.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.adapter.ProductsAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentTabBinding
import com.example.e_commerce_payment.models.CategoriesItems
import com.example.e_commerce_payment.models.ProductItems
import com.example.e_commerce_payment.models.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TabFragment : Fragment() {
    private lateinit var binding: FragmentTabBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var listProductItems: ArrayList<ProductItems>
    private lateinit var rvProducts: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val idCategoryArr = arrayOf(
        "smartphones",
        "laptops",
        "fragrances",
        "skincare",
        "groceries",
        "home-decoration",
        "furniture",
        "tops",
        "womens-dresses",
        "womens-shoes",
        "mens-shirts",
        "mens-shoes",
        "mens-watches",
        "womens-watches",
        "womens-bags",
        "womens-jewellery",
        "sunglasses",
        "automotive",
        "motorcycle",
        "lighting"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString("arg_title")

        Log.d("TabFragment", "Success to get category is: ${category}")
        val index: Int = idCategoryArr.indexOf(category)
        if (index >= 0) {
            fetchProductsByCategoryId(index + 1)
        }

        // Set the title
        binding.tvTitle.text = category
    }


    private fun fetchProductsByCategoryId(categoryId: Int) {
        listProductItems = ArrayList()


        val retrofit = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = retrofit.getProductsByCategoryId(30, 1, categoryId)
        Log.d("TabFragment", "Success to get products by category id: $categoryId")
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        val productList = it.products
                        Log.d("TabFragment", "Total products is: " + it.total)
                        for (productItems in productList) {
                            Log.d(
                                "TabFragment",
                                "Success to get products by category: " + productItems.productName
                            )

                            listProductItems.add(productItems)
                            productsAdapter = ProductsAdapter(listProductItems, requireContext())
                            binding.rvProducts.adapter = productsAdapter
                        }
                    }
                } else {
                    Log.i("TabFragment", "Failed to get products")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.i("TabFragment", "Failed to connect ${t.message}")
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(layoutInflater)
        rvProducts = binding.rvProducts
        rvProducts.setHasFixedSize(true)
        rvProducts.layoutManager = GridLayoutManager(activity, 2)

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun newInstance(category: CategoriesItems): TabFragment {
            val fragment = TabFragment()
            val args = Bundle().apply {
                putString("arg_title", category.categoryName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::linearLayoutManager.isInitialized) {
            linearLayoutManager = null?: return
        }
    }



}