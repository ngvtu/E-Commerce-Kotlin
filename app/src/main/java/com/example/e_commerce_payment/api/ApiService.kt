package com.example.e_commerce_payment.api

import com.example.e_commerce_payment.models.CategoriesResponse
import com.example.e_commerce_payment.models.ProductResponse
import com.example.e_commerce_payment.models.ProductsInCartItems
import com.example.e_commerce_payment.models.ProductsInCartResponse
import com.example.e_commerce_payment.models.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirmation") passwordConfirmation: String
    ): Call<SignUpResponse>


    @FormUrlEncoded
    @POST("sign-in")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("products?limit=30&page=1&sortBy=createdAt=asc")
    fun getProducts(): Call<ProductResponse>

    @GET("categories")
    fun getCategories(): Call<CategoriesResponse>

    @GET("me")
    fun getProfile(@Header("Authorization") token: String?): Call<User>

    @GET("products?limit=30&page=1&sortBy=createdAt=asc")
    fun getProductsByCategory(@Query("category") category: String?): Call<ProductResponse>

    @GET("products")
    fun getProductsByCategoryId(
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("categoryId") categoryId: Int?
    ): Call<ProductResponse>

    @GET("activeCart")
    fun getProductsInCart(@Header("Authorization") token: String?): Call<ProductsInCartResponse>

    @FormUrlEncoded
    @POST("cart")
    fun addProductToCart( @Header("Authorization") token: String?,
        @Field("productId") productId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("productSize") productSize: String = "Size 1",
        @Field("productColor") productColor: String = "Color 1",
    ): Call<ProductsInCartItems>
}