package com.example.e_commerce_payment.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.e_commerce_payment.activity.profile.SettingsActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.FragmentProfileBinding
import com.example.e_commerce_payment.models.User
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        getProfile()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imvGotoSettings.setOnClickListener {

            val intentGotoSettings = Intent(context, SettingsActivity::class.java)
            startActivity(intentGotoSettings)
        }
    }
    private fun getProfile() {
        val token: String = "Bearer " +myPreferenceManager.getToken()

        val apiService = ApiConfig.setUpRetrofit().create(ApiService::class.java).getProfile(token)
        apiService.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    Log.d("ProfileFragment", "get profile done!@ ${response.body()}")
                    val user = response.body()
                    binding.tvNameUser.text = user?.fullName
                    binding.tvEmailUser.text = user?.email
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("ProfileFragment", "get profile failed!@ ${t.message}")

            }

        })
    }
}
