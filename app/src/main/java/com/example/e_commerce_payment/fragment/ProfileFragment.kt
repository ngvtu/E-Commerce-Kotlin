package com.example.e_commerce_payment.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.activity.profile.SettingsActivity
import com.example.e_commerce_payment.activity.profile.ShippingAddressActivity
import com.example.e_commerce_payment.databinding.FragmentProfileBinding
import com.example.e_commerce_payment.storage.MyPreferenceManager

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

        binding.cvSettings.setOnClickListener {

            val intentGotoSettings = Intent(context, SettingsActivity::class.java)
            startActivity(intentGotoSettings)
        }

        binding.cvAddresses.setOnClickListener {
            val intentGotoAddress = Intent(context, ShippingAddressActivity::class.java)
            startActivity(intentGotoAddress)
        }
        binding.cvMyOder.setOnClickListener {
            val intentGotoMyOrder = Intent(context, ShippingAddressActivity::class.java)
            startActivity(intentGotoMyOrder)
        }
        binding.cvMyReviews.setOnClickListener {
            val intentGotoMyReviews = Intent(context, ShippingAddressActivity::class.java)
            startActivity(intentGotoMyReviews)
        }
        binding.cvPaymentMethods.setOnClickListener {
            val intentGotoMyWishlist = Intent(context, ShippingAddressActivity::class.java)
            startActivity(intentGotoMyWishlist)
        }
        binding.cvPromoCodes.setOnClickListener {
            val intentGotoMyWishlist = Intent(context, ShippingAddressActivity::class.java)
            startActivity(intentGotoMyWishlist)
        }


    }
    private fun getProfile() {
//        val token: String = "Bearer " +myPreferenceManager.getToken()
//
//        val apiService = ApiConfig.setUpRetrofit().create(ApiService::class.java).getProfile(token)
//        apiService.enqueue(object: Callback<User>{
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                if (response.isSuccessful){
//                    Log.d("ProfileFragment", "get profile done!@ ${response.body()}")
//                    val user = response.body()
//                    binding.tvNameUser.text = user?.fullName
//                    binding.tvEmailUser.text = user?.email
//
//                    myPreferenceManager.saveIdUser(user?.id.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.e("ProfileFragment", "get profile failed!@ ${t.message}")
//            }
//        })
//
        binding.tvNameUser.text = myPreferenceManager.getFullName()
        binding.tvEmailUser.text = myPreferenceManager.getEmail()
//        Glide.with(this@ProfileFragment).load(productsInFavoriteItems.productImg).into(holder.imageItem)
        context?.let { Glide.with(it).load(myPreferenceManager.getImage()).into(binding.imgUser) };
    }
}
