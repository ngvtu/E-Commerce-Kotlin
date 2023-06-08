package com.example.e_commerce_payment.activity.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.adapter.CategoryAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentCategoryBinding
import com.example.e_commerce_payment.models.CategoriesItems
import com.example.e_commerce_payment.models.CategoriesResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var listCategory:ArrayList<CategoriesItems>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var rcvCategory: RecyclerView

    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(requireContext())
    }
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater)
        // Inflate the layout for this fragment
        rcvCategory = binding.rcvListCategory
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        getListCategories()

        return binding.root
    }



    private fun getListCategories() {

        listCategory = ArrayList()

        val retrofit = ApiConfig.setUpRetrofit().create(ApiService::class.java).getCategories()

        retrofit.enqueue(object : Callback<CategoriesResponse> {
            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: Response<CategoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val categories = response.body()
//                    val listCategory = ArrayList<CategoriesItems>()
                    categories?.let {
                        for (category in categories) {
                            listCategory.add(category)
                        }
                        categoryAdapter = CategoryAdapter(listCategory, context)
                        rcvCategory.adapter = categoryAdapter
                        Log.d("CategoryFragment", "Success to get categories!")

                    }
                } else {
                    Log.d("CategoryFragment", "Failed to get categories")
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                Log.d("CategoryFragment", "Failed to connect ${t.message}")
            }
        })
    }
}