package com.example.e_commerce_payment.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.adapter.ItemFavoriteAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentFavoriteBinding
import com.example.e_commerce_payment.models.GetAllFavoriteResponse
import com.example.e_commerce_payment.models.MessagesResponse
import com.example.e_commerce_payment.models.ProductsInFavoriteItems
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment(), ItemFavoriteAdapter.OnProductDeleteListener {
    private val TAG = "FavoriteFragment"
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var listProductsInFavoriteItems: ArrayList<ProductsInFavoriteItems>
    private lateinit var itemFavoriteAdapter: ItemFavoriteAdapter
    private lateinit var rcvListProductsItemInFavorite: RecyclerView
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(requireContext())
    }
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        rcvListProductsItemInFavorite = binding.rcvListItemFavorites
        rcvListProductsItemInFavorite.setHasFixedSize(true)
        rcvListProductsItemInFavorite.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        token = "Bearer " + myPreferenceManager.getToken()

        fetchFavorite()
        addEvents()
        return binding.root
    }

    private fun addEvents() {

    }

    private fun fetchFavorite() {
        listProductsInFavoriteItems = ArrayList()

        val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = apiService.getAllFavoriteItems(token)
        call.enqueue(object: Callback<GetAllFavoriteResponse> {
            override fun onResponse(
                call: Call<GetAllFavoriteResponse>,
                response: Response<GetAllFavoriteResponse>
            ) {
                if (response.isSuccessful){
                    val getAllFavoriteResponse: GetAllFavoriteResponse? = response.body()
                   for (i in getAllFavoriteResponse!!.data){
                       listProductsInFavoriteItems.add(i)
                   }
                    itemFavoriteAdapter = ItemFavoriteAdapter(listProductsInFavoriteItems, context, this@FavoriteFragment )
                    rcvListProductsItemInFavorite.adapter = itemFavoriteAdapter
                }
            }

            override fun onFailure(call: Call<GetAllFavoriteResponse>, t: Throwable) {
                Log.e(TAG, "${t.message}")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onProductDelete(productId: Int) {
        // Gọi API xóa sản phẩm khỏi giỏ hàng
        deleteProductInFavorite(productId)
        updateListAfterProductDelete(productId)
    }

    private fun updateListAfterProductDelete(productId: Int) {
        val updateList = listProductsInFavoriteItems.filter {
            it.id != productId
        }
        itemFavoriteAdapter.setData(updateList)
    }

    private fun deleteProductInFavorite(productId: Int) {
        val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = apiService.deleteItemFavorite(token, productId)
        call.enqueue(object: Callback<MessagesResponse>{
            override fun onResponse(
                call: Call<MessagesResponse>,
                response: Response<MessagesResponse>
            ) {
                if (response.isSuccessful){
                    Log.d(TAG, "Success to delete in favorite ${response.body()?.message}")
                    fetchFavorite()
                } else{
                    Log.e(TAG, "Failed to delete product in cart")
                }
            }

            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
               Log.e(TAG, "Fail to delete product in favorite ${t.message}")
            }

        })

    }
}