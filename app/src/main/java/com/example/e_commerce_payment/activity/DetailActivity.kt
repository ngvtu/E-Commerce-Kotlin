package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityDetailBinding
import com.example.e_commerce_payment.models.ProductItems
import com.example.e_commerce_payment.models.ProductsInCartItems
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var productItems: ProductItems

    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this)
    }

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get data from intent
        getDataFromIntent()
        addEvents()

    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnAddToCart.setOnClickListener {

            bindProgressButton(binding.btnAddToCart)
            binding.btnAddToCart.attachTextChangeAnimator()
            binding.btnAddToCart.showProgress {
                buttonTextRes = R.string.adding
                progressColor = getColor(R.color.WHITE)
            }
            val token: String = "Bearer " +myPreferenceManager.getToken()
            //add to cart
            val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
            val call = apiService.addProductToCart(token, productItems.id, 1)
            call.enqueue(object: Callback<ProductsInCartItems>{
                override fun onResponse(
                    call: retrofit2.Call<ProductsInCartItems>,
                    response: Response<ProductsInCartItems>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@DetailActivity, "Add to cart success!", Toast.LENGTH_SHORT).show()
                        binding.btnAddToCart.hideProgress("Add success!")
                        onBackPressed()
                        finish()
                    }
                    else{
                        Toast.makeText(this@DetailActivity, "Add to cart fail!", Toast.LENGTH_SHORT).show()
                        //add to cart failed
                        binding.btnAddToCart.hideProgress("Add failed!")
                    }
                }

                override fun onFailure(call: retrofit2.Call<ProductsInCartItems>, t: Throwable) {
                    Log.d("DetailActivity", "onFailure: " + t.message)
                    binding.btnAddToCart.hideProgress("Add failed!")
                }
            })
        }
    }

    private fun getDataFromIntent() {
        val bundle: Bundle? = intent.extras
        if (bundle != null){
            productItems = bundle.getSerializable("productItems") as ProductItems
            
            Glide.with(this).load(productItems.productImg).into(binding.imgItem)
            binding.tvNameItem.text = productItems.productName
            binding.tvDescription.text = productItems.productDescription
            binding.tvPrice.text = productItems.sellingPrice.toString()
            binding.tvCategory.text = idCategoryArr[productItems.categoryId - 1]

        }
    }
}