package com.example.e_commerce_payment.activity.admin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityCreateProductsBinding
import com.example.e_commerce_payment.models.CreateProductResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.google.android.material.textfield.TextInputLayout
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class CreateProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProductsBinding
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@CreateProductsActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnAddProduct.setOnClickListener {
            val productName = sanitizeInput(binding.edtProductName.text.toString())
            val productSize = sanitizeInput(binding.edtProductSize.text.toString())
            val productColor = sanitizeInput(binding.edtProductColor.text.toString())
            val discountPrice = sanitizeInput(binding.edtDiscountPrice.text.toString())
            val sellingPrice = sanitizeInput(binding.edtSellPrice.text.toString())
            val productDescription = sanitizeInput(binding.edtDescription.text.toString())
            val categoryId = sanitizeInput(binding.edtCategory.text.toString())
            val productImg = sanitizeInput(binding.edtLinkImage.text.toString())
            val quantity = sanitizeInput(binding.edtQuantity.text.toString())
            val token: String = "Bearer " + myPreferenceManager.getToken()

            if (productName.nonEmpty() && productSize.nonEmpty() && productColor.nonEmpty()
                && discountPrice.nonEmpty() && sellingPrice.nonEmpty() && productDescription.nonEmpty()
                && categoryId.nonEmpty() && productImg.nonEmpty() && quantity.nonEmpty()
            ) {
                val retrofit: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
                val call = retrofit.createProduct(
                    token,
                    productName,
                    productSize,
                    productColor,
                    discountPrice.toInt(),
                    sellingPrice.toInt(),
                    productDescription,
                    categoryId.toInt(),
                    productImg,
                    quantity.toInt()
                )

                call.enqueue(object : Callback<CreateProductResponse> {
                    override fun onResponse(
                        call: Call<CreateProductResponse>,
                        response: Response<CreateProductResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CreateProductsActivity,
                                "Create done!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(
                                "CreateProductsActivity",
                                "Create done! ${response.body()?.message}"
                            )
                            onBackPressed()
                            finish()
                        } else {
                            try {
                                val errorBody = response.errorBody()?.string()
                                val jsonObject = errorBody?.let { JSONObject(it) }
                                val errorMessage = jsonObject?.getString("message")

                                Log.e("CreateProductsActivity", "response fail: $errorMessage")

                            } catch (e: JSONException) {
                                Log.e(
                                    "CreateProductsActivity",
                                    "Error parsing error response: ${e.message}"
                                )
                                // Xử lý lỗi khi không thể phân tích thông báo lỗi
                                // ...
                            }
                        }
                    }

                    override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
                        Log.d("CreateProductsActivity", "OnFail: ${t.message}")
                    }
                })

            } else {
                setErrorIfEmpty(binding.layoutProductName, productName, "Please enter product name")
                setErrorIfEmpty(binding.layoutProductSize, productSize, "Please enter product size")
                setErrorIfEmpty(binding.layoutProductColor, productColor, "Please enter product color")
                setErrorIfEmpty(binding.layoutProductDiscount, discountPrice, "Please enter discount price")
                setErrorIfEmpty(binding.layoutDescription, productDescription, "Please enter product description")
                setErrorIfEmpty(binding.layoutCategory, categoryId, "Please enter category id")
                setErrorIfEmpty(binding.layoutProductImage, productImg, "Please enter link image")

                if (sellingPrice.isNotEmpty() && discountPrice.isNotEmpty()) {
                    if (sellingPrice.toInt() > discountPrice.toInt()) {
                        binding.layoutProductSellPrice.error = "Selling price must be less than discount price"
                        binding.layoutProductSellPrice.editText?.requestFocus()
                    } else {
                        binding.layoutProductSellPrice.error = null
                        setErrorIfEmpty(binding.layoutProductSellPrice, sellingPrice, "Please enter selling price")
                    }
                }
                if (categoryId.isNotEmpty() && (categoryId.toInt() > 21 || categoryId.toInt() < 1)) {
                    binding.layoutCategory.error = "Please enter category id from 1 to 4"
                } else {
                    binding.layoutCategory.error = null
                }
            }
        }
    }

    private fun setErrorIfEmpty(layout: TextInputLayout, field: String, errorText: String) {
        if (field.isEmpty()) {
            layout.error = errorText
            layout.editText?.requestFocus()
        } else {
            layout.error = null
        }
    }

    fun sanitizeInput(input: String): String {
        // Loại bỏ các ký tự có thể tấn công XSS
        val sanitizedInput = input.replace(Regex("[<>&'\"]"), "")

        // Loại bỏ các ký tự có thể tấn công SQL injection
        val pattern = Pattern.compile("[\"';\\\\]")
        val matcher = pattern.matcher(sanitizedInput)
        val safeInput = matcher.replaceAll("")

        return safeInput
    }
}